package com.srz.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.srz.common.valid.AddGroup;
import com.srz.common.valid.UpdataeStatusGroup;
import com.srz.common.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.service.PmsBrandService;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@RestController
@RequestMapping("product/pmsbrand")
public class PmsBrandController {
    @Autowired
    private PmsBrandService pmsBrandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmsbrand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsBrandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:pmsbrand:info")
    public R info(@PathVariable("brandId") Long brandId){
		PmsBrandEntity pmsBrand = pmsBrandService.getById(brandId);

        return R.ok().put("pmsBrand", pmsBrand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsbrand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody PmsBrandEntity pmsBrand/*, BindingResult result*/){
//        //BindingResult result返回错误消息
//        if (result.hasErrors()) {
//            Map<String,String> map = new HashMap<>();
//            //获取数据的校验结果
//            result.getFieldErrors().forEach((item)->{
//
//                // FieldErrors可以获取错误信息
//                String message = item.getDefaultMessage();
//                // 获取错误属性的名字
//                String field = item.getField();
//                map.put(field,message);
//            });
//
//            return R.error(400,"提交的数据不合法").put("data",map);
//        } else {
//            pmsBrandService.save(pmsBrand);
//        }

        pmsBrandService.save(pmsBrand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsbrand:update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody PmsBrandEntity pmsBrand){
		pmsBrandService.updateDetail(pmsBrand);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:pmsbrand:update")
    public R updateStatus(@Validated({UpdataeStatusGroup.class}) @RequestBody PmsBrandEntity pmsBrand){
        pmsBrandService.updateById(pmsBrand);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsbrand:delete")
    public R delete(@RequestBody Long[] brandIds){
		pmsBrandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
