package com.srz.gulimall.product.service.impl;

import com.srz.gulimall.product.vo.AttrGroupReleationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsAttrAttrgroupRelationDao;
import com.srz.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.srz.gulimall.product.service.PmsAttrAttrgroupRelationService;


@Service("pmsAttrAttrgroupRelationService")
public class PmsAttrAttrgroupRelationServiceImpl extends ServiceImpl<PmsAttrAttrgroupRelationDao, PmsAttrAttrgroupRelationEntity> implements PmsAttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrAttrgroupRelationEntity> page = this.page(
                new Query<PmsAttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<PmsAttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBatch(List<AttrGroupReleationVo> vos) {
        List<PmsAttrAttrgroupRelationEntity> collect = vos.stream().map((item) -> {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        this.saveBatch(collect);

    }

}