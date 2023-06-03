package com.srz.gulimall.order.feign;

import com.srz.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author srz
 * @create 2023/5/24 6:42
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {


 @GetMapping("/currentUserCartItems")
 List<OrderItemVo> getCurrentUserCartItems();
}
