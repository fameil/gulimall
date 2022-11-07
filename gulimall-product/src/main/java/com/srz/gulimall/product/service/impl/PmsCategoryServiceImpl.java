package com.srz.gulimall.product.service.impl;

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

import com.srz.gulimall.product.dao.PmsCategoryDao;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.service.PmsCategoryService;


@Service("pmsCategoryService")
public class PmsCategoryServiceImpl extends ServiceImpl<PmsCategoryDao, PmsCategoryEntity> implements PmsCategoryService {

//    @Autowired
//    PmsCategoryDao categoryDao;

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