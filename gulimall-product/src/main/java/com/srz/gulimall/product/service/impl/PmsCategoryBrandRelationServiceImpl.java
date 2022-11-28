package com.srz.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.srz.gulimall.product.dao.PmsBrandDao;
import com.srz.gulimall.product.dao.PmsCategoryDao;
import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsCategoryBrandRelationDao;
import com.srz.gulimall.product.entity.PmsCategoryBrandRelationEntity;
import com.srz.gulimall.product.service.PmsCategoryBrandRelationService;


@Service("pmsCategoryBrandRelationService")
public class PmsCategoryBrandRelationServiceImpl extends ServiceImpl<PmsCategoryBrandRelationDao, PmsCategoryBrandRelationEntity> implements PmsCategoryBrandRelationService {

    @Autowired
    PmsBrandDao brandDao;

    @Autowired
    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryBrandRelationDao relationDao;

    @Autowired
    PmsBrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryBrandRelationEntity> page = this.page(
                new Query<PmsCategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<PmsCategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(PmsCategoryBrandRelationEntity pmsCategoryBrandRelation) {
        Long brandId = pmsCategoryBrandRelation.getBrandId();
        Long catelogId = pmsCategoryBrandRelation.getCatelogId();
        // 查询详细名字

        PmsBrandEntity pmsBrandEntity = brandDao.selectById(brandId);
        PmsCategoryEntity pmsCategoryEntity = categoryDao.selectById(catelogId);
        pmsCategoryBrandRelation.setBrandName(pmsBrandEntity.getName());
        pmsCategoryBrandRelation.setCatelogName(pmsCategoryEntity.getName());

        this.save(pmsCategoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        PmsCategoryBrandRelationEntity relationEntity = new PmsCategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        this.update(relationEntity,new UpdateWrapper<PmsCategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId, name);
    }

    @Override
    public List<PmsBrandEntity> getBrandsByCatid(Long catId) {
        List<PmsCategoryBrandRelationEntity> catelogId = relationDao.selectList(new QueryWrapper<PmsCategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<PmsBrandEntity> collect = catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            PmsBrandEntity byId = brandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());

        return collect;
    }

}