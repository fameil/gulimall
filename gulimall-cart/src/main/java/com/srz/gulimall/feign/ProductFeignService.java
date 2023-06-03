package com.srz.gulimall.feign;

import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author srz
 * @create 2023/5/11 21:14
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("/product/pmsskuinfo/info/{skuId}")
    //@RequiresPermissions("product:pmsskuinfo:info")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/pmsskusaleattrvalue/stringlist/{skuId}")
    List<String> getSkuSaleAttrValue(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/pmsskuinfo/{skuId}/price")
    public BigDecimal getPrice(@PathVariable("skuId") Long skuId);
}
