package com.summersky.gulishop.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summersky.gulishop.product.entity.CategoryEntity;
import com.summersky.gulishop.product.service.CategoryService;
import com.summersky.common.utils.PageUtils;
import com.summersky.common.utils.R;



/**
 * 商品三级分类
 *
 * @author zengfanbin
 * @email zengfanbin@gmail.com
 * @date 2020-07-03 12:08:45
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        List<CategoryEntity> entities = categoryService.listWithTree();


        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     * @RequestBody:获取请求体，必须是POST请求，GET请求是没有请求体的
     * SpringMVC自动将请求体转换成对应的对象
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
        // 删除菜单前需要判断当前菜单是否被别的地方引用
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
