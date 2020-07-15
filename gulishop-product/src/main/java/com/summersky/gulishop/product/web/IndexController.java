package com.summersky.gulishop.product.web;

import com.summersky.gulishop.product.entity.CategoryEntity;
import com.summersky.gulishop.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:22:34
 * @Description:
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    // TODO 查出所有的一级分类


    @GetMapping({"/","index.html"})
    public String indexPage(Model model){

        // TODO 查询出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("category",categoryEntities);

        // 如果直接在templates文件夹下，那么不用加前缀，直接返回页面名称即可
        // 因为thymeleaf默认自带了前缀和后缀
        // 但是如果templates下面还有子文件夹，那么访问子文件夹的资源需要带上子文件夹的名称
        return "index";
    }
}
