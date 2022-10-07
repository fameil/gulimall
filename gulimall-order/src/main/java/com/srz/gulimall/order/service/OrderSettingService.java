package com.srz.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-11 15:42:02
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

