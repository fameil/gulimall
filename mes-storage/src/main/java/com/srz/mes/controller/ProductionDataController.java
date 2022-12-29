package com.srz.mes.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.srz.mes.entity.ProductionDataEntity;
import com.srz.mes.service.ProductionDataService;


import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-08-29 13:01:16
 */
@RestController
@RequestMapping("mes/productiondata")
public class ProductionDataController {
    @Autowired
    private ProductionDataService productionDataService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = productionDataService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ProductionDataEntity productionData = productionDataService.getById(id);

        return R.ok().put("productionData", productionData);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public String save(@RequestBody ProductionDataEntity entity){
/*        HashMap map = JSON.parseObject(json, HashMap.class);
        ProductionDataEntity entity = new ProductionDataEntity();
        entity.setClientId(map.get("clientId")+"");
        entity.setCellId(map.get("cellId")+"");
        entity.setResult(Integer.parseInt(map.get("result")+""));
        entity.setData(map.get("data")+"");
        entity.setNote(map.get("note")+"");*/

        productionDataService.save(entity);

        return entity.toString();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ProductionDataEntity productionData){
		productionDataService.updateById(productionData);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		productionDataService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
