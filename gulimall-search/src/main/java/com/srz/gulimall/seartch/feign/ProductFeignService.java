package com.srz.gulimall.seartch.feign;

import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author srz
 * @create 2023/2/21 5:09
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping ("/product/pmsattr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @GetMapping("/product/pmsbrand/infos")
    public R brandsInfo(@RequestParam("brandIds") List<Long> brandId);



}
