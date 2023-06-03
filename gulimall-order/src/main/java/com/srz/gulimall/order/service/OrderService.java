package com.srz.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.order.entity.OrderEntity;
import com.srz.gulimall.order.vo.OrderConfirmVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-11 15:42:02
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @return 订单页确认页需要用的数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;
}

