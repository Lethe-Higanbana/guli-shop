package com.summersky.gulishop.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summersky.gulishop.product.service.CategoryBrandRelationService;
import com.summersky.gulishop.product.vo.Catelog2Vo;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summersky.common.utils.PageUtils;
import com.summersky.common.utils.Query;

import com.summersky.gulishop.product.dao.CategoryDao;
import com.summersky.gulishop.product.entity.CategoryEntity;
import com.summersky.gulishop.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author Lenovo
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 组装父子树形结构
        // 找到所有一级分类
        /**
         * Lambda表达式：参数 -> 函数主体，lambda表达式会根据上下文自动识别参数类型，可以不手动声明参数类型
         * stream()：流式操作，用来操作集合
         */
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((menu)->{
                    menu.setChildren(getChildrens(menu,entities));
                    return menu;
                }).sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param category
     * 缓存数据一致性：失效模式，修改数据库，删除缓存
     * @CacheEvict：触发将数据从缓存删除的操作
     */
    @CacheEvict(value = "category",key = "'level1Categorys'")
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    /**
     * 查出所有一级分类
     * @return
     * @Cacheable：表示当前方法的结果需要缓存，如果缓存中有，则方法都不用调用。如果缓存中没有，则调用方法，最后将方法的结果放入缓存，在（）里面指定数据要缓存到哪个缓存分区下面
     */
    @Cacheable(value = {"category"},key = "'level1Categorys'")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCataLogJson(){
        // 放入缓存的数据是json字符串，取出来的是json字符串，还需要逆转为我们需要的对象【序列化与反序列化】
        /**
         * 1、空结果缓存：解决缓存穿透
         * 2、设置过期时间（加随机值）：解决缓存雪崩
         * 3、加锁：解决缓存击穿
         */
        // 加入缓存逻辑,缓存中存的数据是JSON，因为JSON是跨语言、跨平台的兼容
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        // 判断缓存中是否有数据
        if (StringUtils.isEmpty(catalogJSON)){
            // 缓存中没有，查询数据库
            // 调用查询数据库的方法,每次请求都调用查询数据库，性能低下，所以需要加入缓存
            Map<String, List<Catelog2Vo>> cataLogJsonFromDb = getCataLogJsonFromDb();
            // 返回结果
            return cataLogJsonFromDb;
        }

        // 转化为我们需要的数据对象
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        return result;
    }

    /**
     * Redisson操作分布式锁
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCataLogFromRedissonLock(){
        // 加锁
        RLock lock = redisson.getLock("CataLogJson-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDB;
        try{
            // 加锁成功，执行业务
            dataFromDB = getStringDataFromDB();
        }finally {
            // 解锁
            lock.unlock();
        }
        return dataFromDB;
    }

    /**
     * redis分布式锁
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCataLogFromRedisLock(){
        // 设置唯一ID，防止业务超时删除别人的锁
        String uuid = UUID.randomUUID().toString();
        // 占分布式锁，去redis占坑,setIfAbsent相当于SETNX命令，只有在redis中没有那个数据才能设置成功，否则失败
        // 加锁必须设置过期时间，否则会造成死锁问题，而且，加锁和设置过期时间必须是同步进行（原子操作）
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,30,TimeUnit.SECONDS);
        if (lock){
            Map<String, List<Catelog2Vo>> dataFromDB;
            try{
                // 加锁成功，执行业务
                dataFromDB = getStringDataFromDB();
            }finally {
                String script = "if redis.call('get',KEYS[1] == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                // 原子删锁
                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            // 业务执行完成，判断是否是自己的锁，是则释放锁
            /*String lockValue = stringRedisTemplate.opsForValue().get("lock");
            if (uuid.equals(lockValue)){
                stringRedisTemplate.delete("lock");
            }*/

            return dataFromDB;
        }else {
            // 加锁失败，重试，自旋的方式,设置休眠防止内存被占满
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCataLogFromRedisLock();
        }
    }

    /**
     * 从数据库查询封装数据
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCataLogJsonFromDb() {
        // springboot所有组件在容器中都是单例的，操作this都是操作的同一个对象
        // TODO 本地锁：synchronized、JUC（Lock），在分布式情况下，想要锁住所有，必须使用分布式锁
        // 同步锁
        synchronized (this){
            return getStringDataFromDB();
        }
    }

    private Map<String, List<Catelog2Vo>> getStringDataFromDB() {
        // 加入缓存逻辑,缓存中存的数据是JSON，因为JSON是跨语言、跨平台的兼容
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        // 加锁之后还需要判断缓存是否存在数据
        if (!StringUtils.isEmpty(catalogJSON)){
            // 如果不为空，转化为我们需要的数据对象直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});
            return result;
        }
        System.out.println("查询了数据库。。。。。");
        // 将多次查询数据库变为一次查询
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // 查出所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList,0L);
        // 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList,v.getCatId());
            // 封装上面的数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    // 找当前二级分类的三级分类
                    List<CategoryEntity> level3Catalog = getParent_cid(selectList,l2.getCatId());
                    if (level3Catalog != null) {
                        // 封装成指定格式
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catalog.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());

            }
            return catelog2Vos;
        }));
        // 将查询出来的数据转为JSON放入缓存
        String s = JSON.toJSONString(parent_cid);
        // 不管查询的数据是否为空，都要将数据放入缓存，包括null结果，防止缓存穿透，并且设置过期时间
        stringRedisTemplate.opsForValue().set("catalogJSON",s,1, TimeUnit.DAYS);
        return parent_cid;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parent_cid) {
        // 从当前的所有数据中筛选出指定 parent_cid 的数据
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid().equals(parent_cid)).collect(Collectors.toList());
        // return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return collect;
    }


    /**
     * 递归查找所有菜单的子菜单
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            // 如果当前菜单的父级id与root的id相同，那么当前菜单为root的子菜单
            return categoryEntity.getParentCid().equals(root.getCatId());
            // .map():可以将一个对象转换成另一个对象
        }).map(categoryEntity -> {
            //1、找到子菜单，递归(自己调用自己)
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //2、菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;

    }
}