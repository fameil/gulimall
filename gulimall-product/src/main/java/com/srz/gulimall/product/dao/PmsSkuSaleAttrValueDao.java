package com.srz.gulimall.product.dao;

import com.srz.gulimall.product.entity.PmsSkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.srz.gulimall.product.vo.SkuItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:57
 */
@Mapper
public interface PmsSkuSaleAttrValueDao extends BaseMapper<PmsSkuSaleAttrValueEntity> {

    List<SkuItemVo.SkuItemSaleAttrsVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleAttrValueAsStringList(@Param("skuId") Long skuId);
}
