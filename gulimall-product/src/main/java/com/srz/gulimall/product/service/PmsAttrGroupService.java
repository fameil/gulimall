package com.srz.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.product.entity.PmsAttrGroupEntity;
import com.srz.gulimall.product.vo.AttrGroupWithAttrsVo;
import com.srz.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-06 16:28:58
 */
public interface PmsAttrGroupService extends IService<PmsAttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    List<SkuItemVo.SputItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}

