package com.srz.gulimall.product.app;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.srz.gulimall.product.entity.PmsSkuInfoEntity;
import com.srz.gulimall.product.service.PmsSkuInfoService;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.R;



/**
 * sku信息
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:57
 */
@RestController
@RequestMapping("product/pmsskuinfo")
public class PmsSkuInfoController {
    @Autowired
    private PmsSkuInfoService pmsSkuInfoService;


    @GetMapping("/{skuId}/price")
    public BigDecimal getPrice(@PathVariable("skuId") Long skuId){
        PmsSkuInfoEntity byId = pmsSkuInfoService.getById(skuId);
        return byId.getPrice();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:pmsskuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pmsSkuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    //@RequiresPermissions("product:pmsskuinfo:info")
    public R info(@PathVariable("skuId") Long skuId){
		PmsSkuInfoEntity pmsSkuInfo = pmsSkuInfoService.getById(skuId);

        return R.ok().put("pmsSkuInfo", pmsSkuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:pmsskuinfo:save")
    public R save(@RequestBody PmsSkuInfoEntity pmsSkuInfo){
        pmsSkuInfoService.save(pmsSkuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:pmsskuinfo:update")
    public R update(@RequestBody PmsSkuInfoEntity pmsSkuInfo){
		pmsSkuInfoService.updateById(pmsSkuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:pmsskuinfo:delete")
    public R delete(@RequestBody Long[] skuIds){
		pmsSkuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
