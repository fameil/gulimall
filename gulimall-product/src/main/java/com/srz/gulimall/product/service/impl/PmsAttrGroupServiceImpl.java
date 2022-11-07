package com.srz.gulimall.product.service.impl;

import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsAttrGroupDao;
import com.srz.gulimall.product.entity.PmsAttrGroupEntity;
import com.srz.gulimall.product.service.PmsAttrGroupService;


@Service("pmsAttrGroupService")
public class PmsAttrGroupServiceImpl extends ServiceImpl<PmsAttrGroupDao, PmsAttrGroupEntity> implements PmsAttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrGroupEntity> page = this.page(
                new Query<PmsAttrGroupEntity>().getPage(params),
                new QueryWrapper<PmsAttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if(catelogId ==0 ){
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params),
                    new QueryWrapper<PmsAttrGroupEntity>());
            return new PageUtils(page);
        } else {
            String key = (String) params.get("key");
            // select * from pms_attr_group where catelog_id = ? and (attr_group_id = key or attr_group_name like %key%)
            QueryWrapper<PmsAttrGroupEntity> wapper = new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id",catelogId);
            if(!StringUtils.isNullOrEmpty(key)){
                wapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params),
                    wapper);
            return new PageUtils(page);
        }

    }


}