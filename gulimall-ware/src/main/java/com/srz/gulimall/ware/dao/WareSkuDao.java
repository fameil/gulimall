package com.srz.gulimall.ware.dao;

import com.srz.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-11 16:30:53
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}