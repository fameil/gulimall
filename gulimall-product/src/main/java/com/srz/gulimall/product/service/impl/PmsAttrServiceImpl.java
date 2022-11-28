package com.srz.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.srz.common.constant.ProductConstant;
import com.srz.gulimall.product.dao.PmsAttrAttrgroupRelationDao;
import com.srz.gulimall.product.dao.PmsAttrGroupDao;
import com.srz.gulimall.product.dao.PmsCategoryDao;
import com.srz.gulimall.product.entity.PmsAttrAttrgroupRelationEntity;
import com.srz.gulimall.product.entity.PmsAttrGroupEntity;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import com.srz.gulimall.product.vo.AttrGroupReleationVo;
import com.srz.gulimall.product.vo.AttrRespVo;
import com.srz.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsAttrDao;
import com.srz.gulimall.product.entity.PmsAttrEntity;
import com.srz.gulimall.product.service.PmsAttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsAttrService")
public class PmsAttrServiceImpl extends ServiceImpl<PmsAttrDao, PmsAttrEntity> implements PmsAttrService {

    @Autowired
    PmsAttrAttrgroupRelationDao relationDao;

    @Autowired
    PmsAttrGroupDao attrGroupDao;

    @Autowired
    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryService pmsCategoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                new QueryWrapper<PmsAttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attrVo) {
        PmsAttrEntity attrEntity = new PmsAttrEntity();
        //保存基本数据
        BeanUtils.copyProperties(attrVo,attrEntity);
        this.save(attrEntity);

        if(attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attrVo.getAttrGroupId()!=null){
            //保存关联关系
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils  queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<PmsAttrEntity> queryWrapper = new QueryWrapper<PmsAttrEntity>().
                eq("attr_type","base".equalsIgnoreCase(attrType)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId != ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()){
            queryWrapper.eq("catelog_id",catelogId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<PmsAttrEntity> page = this.page(
                new Query<PmsAttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<PmsAttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            if("base".equalsIgnoreCase(attrType)){
                // 设置分类和分组的名字
                PmsAttrAttrgroupRelationEntity attr_id = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attr_id != null && attr_id.getAttrGroupId()!=null) {
                    PmsAttrGroupEntity pmsAttrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
                    attrRespVo.setGroupName(pmsAttrGroupEntity.getAttrGroupName());
                }
            }

            PmsCategoryEntity pmsCategoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (pmsCategoryEntity != null) {
                attrRespVo.setCatelogName(pmsCategoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        PmsAttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity,respVo);


        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){

            // 设置分组信息
            PmsAttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relationEntity!=null){
                respVo.setAttrGroupId(relationEntity.getAttrGroupId());
                PmsAttrGroupEntity pmsAttrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (pmsAttrGroupEntity!=null){
                    respVo.setCatelogName(pmsAttrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = pmsCategoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(catelogPath);

        PmsCategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity!=null){
            respVo.setCatelogName(categoryEntity.getName());
        }


        return respVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attrVo) {
        PmsAttrEntity attrEntity = new PmsAttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        this.updateById(attrEntity);

        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){

            //修改分组关联
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrVo.getAttrId());

            Long count = relationDao.selectCount(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id", attrVo.getAttrId()));
            if (count > 0){

                relationDao.update(relationEntity,new UpdateWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_id",attrVo.getAttrId()));
            } else {
                relationDao.insert(relationEntity);
            }
        }

    }

    /**
     * 根据分组id查找关联的所有属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<PmsAttrEntity> getRelationAttr(Long attrgroupId) {
        List<PmsAttrAttrgroupRelationEntity> entities = relationDao
                .selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if (attrIds == null || attrIds.size() == 0){
            return null;
        }
        List<PmsAttrEntity> entities1 = this.listByIds(attrIds);

        return entities1;
    }

    @Override
    public void deleteRelation(AttrGroupReleationVo[] vos) {
        //relationDao.delete(new QueryWrapper<>().eq("attr_id",1L).eq("attr_group_id",1L));

        List<PmsAttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            PmsAttrAttrgroupRelationEntity relationEntity = new PmsAttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        relationDao.deleteBatchRelation(entities);

    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        PmsAttrGroupEntity pmsAttrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = pmsAttrGroupEntity.getCatelogId();
        //2、当前分组只能关联的分组没有引用的属性
        //2.1)、当前分类下的其他分组
        List<PmsAttrGroupEntity> entityList = attrGroupDao.selectList(new QueryWrapper<PmsAttrGroupEntity>()
                .eq("catelog_id", catelogId)
        );
        List<Long> collect = entityList.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2)、这些分组关联的属性
        List<PmsAttrAttrgroupRelationEntity> groupId = relationDao.selectList(new QueryWrapper<PmsAttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //2.3)、从当前分类的所有属性中移除这些属性;
        QueryWrapper<PmsAttrEntity> queryWrapper = new QueryWrapper<PmsAttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds !=null && attrIds.size()>0){
            queryWrapper.notIn("attr_id", attrIds);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((wapper)->{
                wapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<PmsAttrEntity> page = this.page(new Query<PmsAttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

}



















