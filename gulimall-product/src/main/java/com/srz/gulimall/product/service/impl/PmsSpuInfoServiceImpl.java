package com.srz.gulimall.product.service.impl;


import com.srz.common.to.SkuReductionTo;
import com.srz.common.to.SpuBoundTo;
import com.srz.common.utils.R;
import com.srz.gulimall.product.entity.*;
import com.srz.gulimall.product.feign.CouponFeignService;
import com.srz.gulimall.product.service.*;
import com.srz.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsSpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("pmsSpuInfoService")
public class PmsSpuInfoServiceImpl extends ServiceImpl<PmsSpuInfoDao, PmsSpuInfoEntity> implements PmsSpuInfoService {

    @Autowired
    PmsSpuInfoDescService pmsSpuInfoDescService;

    @Autowired
    PmsSpuImagesService imagesService;

    @Autowired
    PmsAttrService attrService;

    @Autowired
    PmsProductAttrValueService productAttrValueService;

    @Autowired
    PmsSkuInfoService skuInfoService;

    @Autowired
    PmsSkuImagesService skuImagesService;

    @Autowired
    PmsSkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;




    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo saveVo) {
        //1、保存spu基本信息pms_spu_info
        PmsSpuInfoEntity infoEntity = new PmsSpuInfoEntity();
        BeanUtils.copyProperties(saveVo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2、保存Spu的描述图片pms_spu_info_desc
        List<String> decript = saveVo.getDecript();
        PmsSpuInfoDescEntity descEntity = new PmsSpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        pmsSpuInfoDescService.saveSpuInfoDesc(descEntity);


        //3、保存spu的图片集pms_spu_images
        List<String> images = saveVo.getImages();
        imagesService.saveImages(infoEntity.getId(),images);



        //4、保存spu的规格参数pms_sku_sale_attr_value
        List<BaseAttrs> baseAttrs = saveVo.getBaseAttrs();
        List<PmsProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            PmsProductAttrValueEntity valueEntity = new PmsProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            PmsAttrEntity byId = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(byId.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);


        //5、保存spu的积分信息gulimall_sms->sms_spu_bounds
        Bounds bounds = saveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());

        R r = couponFeignService.saveSpuBouds(spuBoundTo);
        if (r.getCode() != 0){
            log.error("远程保存spu积分信息失败");
        }

        //5、保存当前spu对应的所有sku信息
        List<Skus> skus = saveVo.getSkus();
        if (skus !=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg = image.getImgUrl();
                    }
                }
                PmsSkuInfoEntity pmsSkuInfoEntity = new PmsSkuInfoEntity();
                BeanUtils.copyProperties(item,pmsSkuInfoEntity);
                pmsSkuInfoEntity.setBrandId(infoEntity.getBrandId());
                pmsSkuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                pmsSkuInfoEntity.setSaleCount(0L);
                pmsSkuInfoEntity.setSpuId(infoEntity.getId());
                pmsSkuInfoEntity.setSkuDefaultImg(defaultImg);

                //5.1、sku的基本信息pms_sku_info
                skuInfoService.saveSkuInfo(pmsSkuInfoEntity);

                Long skuId = pmsSkuInfoEntity.getSkuId();


                List<PmsSkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    PmsSkuImagesEntity skuImagesEntity = new PmsSkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());

                    return skuImagesEntity;
                }).collect(Collectors.toList());

                //5.2、sku的图片信息pms_sku_images
                skuImagesService.saveBatch(imagesEntities);
                //TODO 没有图片，路径的无需保存

                List<Attr> attr = item.getAttr();
                List<PmsSkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    PmsSkuSaleAttrValueEntity attrValueEntity = new PmsSkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());

                //5.3、sku的销售属性信息pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                //5.4、sku的优惠、满减等信息gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                //属性无法对对拷
                List<com.srz.common.to.MemberPrice> memberPriceList = new ArrayList<>(item.getMemberPrice().size());

                item.getMemberPrice().forEach(memberPrice -> {
                    com.srz.common.to.MemberPrice price = new com.srz.common.to.MemberPrice();
                    price.setId(memberPrice.getId());
                    price.setName(memberPrice.getName());
                    price.setPrice(memberPrice.getPrice());
                    memberPriceList.add(price);
                });
                skuReductionTo.setMemberPrice(memberPriceList);


                R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                if (r2.getCode() != 0){
                    log.error("远程保存sku优惠信息失败");
                }

            });
        }

    }

    @Override
    public void  saveBaseSpuInfo(PmsSpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }



}