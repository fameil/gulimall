package com.srz.gulimall.product.service.impl;

import com.srz.gulimall.product.entity.PmsSpuInfoEntity;
import com.srz.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsSkuInfoDao;
import com.srz.gulimall.product.entity.PmsSkuInfoEntity;
import com.srz.gulimall.product.service.PmsSkuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSkuInfoService")
public class PmsSkuInfoServiceImpl extends ServiceImpl<PmsSkuInfoDao, PmsSkuInfoEntity> implements PmsSkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSkuInfoEntity> page = this.page(
                new Query<PmsSkuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(PmsSkuInfoEntity pmsSkuInfoEntity) {
        this.baseMapper.insert(pmsSkuInfoEntity);
    }


}