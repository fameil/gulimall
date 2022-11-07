package com.srz.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.R;



/**
 * 商品三级分类
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@RestController
@RequestMapping("product/pmscategory")
public class PmsCategoryController {
    @Autowired
    private PmsCategoryService pmsCategoryService;

    /**
     * 查出所有分类以及子父类，以树状结构组装起来
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product:pmscategory:list")
    public R list(){
        List<PmsCategoryEntity> entities = pmsCategoryService.listWithTree();



        return R.ok().put("data", entities);
    }


    /** 
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:pmscategory:info")
    public R info(@PathVariable("catId") Long catId){
		PmsCategoryEntity pmsCategory = pmsCategoryService.getById(catId);

        return R.ok().put("data", pmsCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmscategory:save")
    public R save(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.save(pmsCategory);

        return R.ok();
    }

    /**
     *
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:pmscategory:update")
    public R updateSort(@RequestBody PmsCategoryEntity[] pmsCategory){
        pmsCategoryService.updateBatchById(Arrays.asList(pmsCategory));
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmscategory:update")
    public R update(@RequestBody PmsCategoryEntity pmsCategory){
		pmsCategoryService.updateById(pmsCategory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmscategory:delete")
    public R delete(@RequestBody Long[] catIds){

        //1、检查当前删除的菜单，是否被别的地方引用
		pmsCategoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
