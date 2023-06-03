package com.srz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.product.entity.PmsSkuInfoEntity;
import com.srz.gulimall.product.entity.PmsSpuInfoEntity;
import com.srz.gulimall.product.vo.SkuItemVo;
import com.srz.gulimall.product.vo.SpuSaveVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:57
 */
public interface PmsSkuInfoService extends IService<PmsSkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveSkuInfo(PmsSkuInfoEntity pmsSkuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<PmsSkuInfoEntity> getSpusBySpuID(Long spuId);

    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}

