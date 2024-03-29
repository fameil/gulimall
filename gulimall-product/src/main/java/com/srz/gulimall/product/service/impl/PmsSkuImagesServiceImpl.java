package com.srz.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsSkuImagesDao;
import com.srz.gulimall.product.entity.PmsSkuImagesEntity;
import com.srz.gulimall.product.service.PmsSkuImagesService;


@Service("pmsSkuImagesService")
public class PmsSkuImagesServiceImpl extends ServiceImpl<PmsSkuImagesDao, PmsSkuImagesEntity> implements PmsSkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSkuImagesEntity> page = this.page(
                new Query<PmsSkuImagesEntity>().getPage(params),
                new QueryWrapper<PmsSkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsSkuImagesEntity> getImagesBySkuId(Long skuId) {
        PmsSkuImagesDao imagesDao = this.baseMapper;

        List<PmsSkuImagesEntity> imagesEntities = imagesDao.selectList(new QueryWrapper<PmsSkuImagesEntity>().eq("sku_id", skuId));
        return imagesEntities;
    }

}