package com.srz.gulimall.coupon.dao;

import com.srz.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-08 22:19:59
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
