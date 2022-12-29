package com.srz.gulimall.product.service.impl;

import com.srz.gulimall.product.service.PmsCategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsCategoryDao;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

//    @Autowired
//    PmsCategoryDao categoryDao;

    @Autowired
    PmsCategoryBrandRelationService pmsCategoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsCategoryEntity> page = this.page(
                new Query<PmsCategoryEntity>().getPage(params),
                new QueryWrapper<PmsCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PmsCategoryEntity> listWithTree() {
        //1、查出所有分类
        List<PmsCategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构
        List<PmsCategoryEntity> collect = entities.stream().filter(e -> e.getParentCid() == 0)
                .map((menu)->{
                    menu.setChildren(getChilderns(menu,entities));
                    return menu;
                }).sorted((menu1,menu2)->{
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1、检查当前删除的菜单，是否被别的地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //2,25,225
    @Override
    public Long[] findCatelogPath(Long attrGroupId1) {
        List<Long> paths = new ArrayList<>();
        findParentPath(attrGroupId1,paths);

        Collections.reverse(paths);

        return (Long[]) paths.toArray(new Long[paths.size()]);
    }

    /**
     * 级联更新所有关联的数据
     * @param pmsCategory
     */
    @Transactional
    @Override
    public void updateCascade(PmsCategoryEntity pmsCategory) {
        this.updateById(pmsCategory);
        pmsCategoryBrandRelationService.updateCategory(pmsCategory.getCatId(),pmsCategory.getName());

    }

    @Override
    public List<PmsCategoryEntity> getLevel1Categorys() {
        List<PmsCategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<PmsCategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    //225,25,2
    private List<Long> findParentPath(Long attrGroupId1,List<Long> paths){
        //1、 收集当前节点id
        paths.add(attrGroupId1);
        PmsCategoryEntity byId = this.getById(attrGroupId1);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }

        return paths;
    }

    //递归查找所有的子菜单
    private List<PmsCategoryEntity> getChilderns(PmsCategoryEntity root, List<PmsCategoryEntity> all){

        List<PmsCategoryEntity> children = all.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    //1、找到子菜单
                    categoryEntity.setChildren(getChilderns(categoryEntity,all));
                    return categoryEntity;
                }).sorted((menu1,menu2) -> {
                    //2、菜单的排序
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                }).collect(Collectors.toList());

        return children;
    }

}