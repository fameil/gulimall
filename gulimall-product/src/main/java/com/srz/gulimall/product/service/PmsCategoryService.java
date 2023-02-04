package com.srz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.product.entity.PmsCategoryEntity;
import com.srz.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
public interface PmsCategoryService extends IService<PmsCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PmsCategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId完整路径
     * [父/子/孙]
     * @param attrGroupId1
     * @return
     */
    Long[] findCatelogPath(Long attrGroupId1);

    void updateCascade(PmsCategoryEntity pmsCategory);

    List<PmsCategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();

}

