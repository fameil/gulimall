package com.srz.mes.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.srz.mes.dao.ProductionDataDao;
import com.srz.mes.entity.ProductionDataEntity;
import com.srz.mes.service.ProductionDataService;


@Service("productionDataService")
public class ProductionDataServiceImpl extends ServiceImpl<ProductionDataDao, ProductionDataEntity> implements ProductionDataService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductionDataEntity> page = this.page(
                new Query<ProductionDataEntity>().getPage(params),
                new QueryWrapper<ProductionDataEntity>()
        );

        return new PageUtils(page);
    }

}