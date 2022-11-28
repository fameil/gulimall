package com.srz.gulimall.product.feign;

import com.srz.common.to.SkuReductionTo;
import com.srz.common.to.SpuBoundTo;
import com.srz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author srz
 * @create 2022/11/27 16:34
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 1、@RequestBody将这个对象转为json
     * 2、找到gulimall-coupon服务，给/coupon/spubounds/save发送请求。
     *      将上一步转的json放在请求体位置，发送请求。
     * 3、对方服务收到请求，请求体里有json数据、
     *  只要json里面的数据模型是兼容的就能转换
     *  双方服务无需使用同一个to
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBouds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
