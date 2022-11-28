package com.srz.gulimall.product.service.impl;

import com.srz.gulimall.product.service.PmsCategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsBrandDao;
import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.service.PmsBrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsBrandService")
public class PmsBrandServiceImpl extends ServiceImpl<PmsBrandDao, PmsBrandEntity> implements PmsBrandService {

    @Autowired
    PmsCategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 1. 获取Key
        String key = (String) params.get("key");
        QueryWrapper<PmsBrandEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("brand_id",key).or().like("name",key);
        }
        IPage<PmsBrandEntity> page = this.page(
                new Query<PmsBrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(PmsBrandEntity pmsBrand) {
        //保证冗余字段的数据一致
        this.updateById(pmsBrand);
        if(!StringUtils.isEmpty(pmsBrand.getName())){
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateBrand(pmsBrand.getBrandId(),pmsBrand.getName());

            //TODO 更新其他关联
        }

    }

}











