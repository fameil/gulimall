package com.srz.gulimall.product.feign;

import com.srz.common.to.es.SkuEsModel;
import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author srz
 * @create 2022/12/23 10:09
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    //上架商品
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
    }
