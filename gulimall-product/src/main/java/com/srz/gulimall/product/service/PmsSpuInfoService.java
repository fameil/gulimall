package com.srz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.product.entity.PmsSpuInfoEntity;
import com.srz.gulimall.product.vo.SpuSaveVo;

import java.util.List;
import java.util.Map;

/**
 * spu信息
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:57
 */
public interface PmsSpuInfoService extends IService<PmsSpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo saveVo);

    void saveBaseSpuInfo(PmsSpuInfoEntity infoEntity);



}

