package com.srz.gulimall.product.service.impl;

import com.srz.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsSkuSaleAttrValueDao;
import com.srz.gulimall.product.entity.PmsSkuSaleAttrValueEntity;
import com.srz.gulimall.product.service.PmsSkuSaleAttrValueService;


@Service("pmsSkuSaleAttrValueService")
public class PmsSkuSaleAttrValueServiceImpl extends ServiceImpl<PmsSkuSaleAttrValueDao, PmsSkuSaleAttrValueEntity> implements PmsSkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSkuSaleAttrValueEntity> page = this.page(
                new Query<PmsSkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<PmsSkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemVo.SkuItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId) {
        PmsSkuSaleAttrValueDao dao = this.baseMapper;
        List<SkuItemVo.SkuItemSaleAttrsVo> saleAttrsVos =  dao.getSaleAttrsBySpuId(spuId);


        return saleAttrsVos;
    }

    @Override
    public List<String> getSkuSaleAttrValueAsStringList(Long skuId) {
        PmsSkuSaleAttrValueDao dao = this.baseMapper;

        return dao.getSkuSaleAttrValueAsStringList(skuId);
    }

}