package com.srz.gulimall.seartch.controller;

import com.srz.gulimall.seartch.service.MallSearchService;
import com.srz.gulimall.seartch.vo.SearchParam;
import com.srz.gulimall.seartch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author srz
 * @create 2023/2/1 9:29
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有参数封装成对象
     * @param parm
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam parm, Model model, HttpServletRequest request) {


        parm.set_queryString(request.getQueryString());
        //1、根据传递来的页面的查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(parm);
        model.addAttribute("result",result);


        return "list";
    }
}
