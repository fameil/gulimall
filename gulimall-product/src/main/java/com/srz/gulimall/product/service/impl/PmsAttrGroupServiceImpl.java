package com.srz.gulimall.product.service.impl;

import com.qiniu.util.StringUtils;
import com.srz.gulimall.product.entity.PmsAttrEntity;
import com.srz.gulimall.product.service.PmsAttrService;
import com.srz.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    PmsAttrService attrService;

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
        String key = (String) params.get("key");
        // select * from pms_attr_group where catelog_id = ? and (attr_group_id = key or attr_group_name like %key%)
        QueryWrapper<PmsAttrGroupEntity> wapper = new QueryWrapper<PmsAttrGroupEntity>();
        if(!StringUtils.isNullOrEmpty(key)){
            wapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if(catelogId ==0 ){
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params),
                    wapper);
            return new PageUtils(page);
        } else {
            wapper.eq("catelog_id",catelogId);
            IPage<PmsAttrGroupEntity> page = this.page(new Query<PmsAttrGroupEntity>().getPage(params),
                    wapper);
            return new PageUtils(page);
        }

    }

    /**
     * 根据分类id查出所有的分组以及这些组里面的属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        //1、查询分组信息
        List<PmsAttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<PmsAttrGroupEntity>().eq("catelog_id", catelogId));

        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, attrsVo);

            List<PmsAttrEntity> attrs = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);

            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }


}