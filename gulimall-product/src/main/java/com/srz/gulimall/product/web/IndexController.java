package com.srz.gulimall.product.web;

import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author srz
 * @create 2022/12/29 15:00
 */
@Controller
public class IndexController {

    @Autowired
    PmsCategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        //TODO 1、查出所有的1级分类
        List<PmsCategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        //视图解析器进行拼串:
        // classpath:/templates/ +返回值+.htmL
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }
}
