package com.srz.gulimall.product.dao;

import com.srz.gulimall.product.entity.PmsAttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.srz.gulimall.product.vo.SkuItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@Mapper
public interface PmsAttrGroupDao extends BaseMapper<PmsAttrGroupEntity> {

    List<SkuItemVo.SputItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
