package com.srz.gulimall.product.dao;

import com.srz.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
@Mapper
public interface PmsAttrAttrgroupRelationDao extends BaseMapper<PmsAttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("entities") List<PmsAttrAttrgroupRelationEntity> entities);
}
