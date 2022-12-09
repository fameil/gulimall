package com.srz.gulimall.ware.feign;

import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author srz
 * @create 2022/12/3 17:47
 */

@FeignClient("gulimall-product")
public interface ProductFeignService {

//    1)、让所有请求过网关;
//        1、@FeignClient ient( "gulimall-gateway"):给gulimall-gateway所在的机器发请求
//        2、/api/product/skuinfo/info/{skuId}
//    2)、直接让后台指定服务处理
//        1、@FeignClient( "gulimall-gateway")
//        2、/product/skuinfo/info/{skuId}


    //不通过网关
    @RequestMapping("/product/pmsskuinfo/info/{skuId}")
    //@RequiresPermissions("product:pmsskuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);
}
