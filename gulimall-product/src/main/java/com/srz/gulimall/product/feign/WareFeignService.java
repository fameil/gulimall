package com.srz.gulimall.product.feign;

import com.srz.common.to.SkuHasStockVo;
import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author srz
 * @create 2022/12/15 23:03
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * 1、设计的时候该上泛型
     * 2、直接返回我们想要的结果
     * 3、自己封装解析结果
     * @param skuIds
     * @return
     */
    //查询sku是否有库存
    @PostMapping("/ware/waresku/hasstock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds);
}
