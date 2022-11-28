package com.srz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.entity.PmsCategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author srz
 * @email srz@gmail.com
 * @date 2022-11-11 01:46:03
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);

    List<PmsBrandEntity> getBrandsByCatid(Long catId);

}

