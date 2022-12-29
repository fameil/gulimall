package com.srz.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.srz.common.utils.PageUtils;
import com.srz.mes.entity.ProductionDataEntity;

import java.util.Map;

/**
 * 
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-08-29 13:01:16
 */
public interface ProductionDataService extends IService<ProductionDataEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

