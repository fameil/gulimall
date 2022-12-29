package com.srz.gulimall.seartch.service;

import com.srz.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author srz
 * @create 2022/12/23 0:12
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
