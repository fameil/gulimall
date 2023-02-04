package com.srz.gulimall.product.service.impl;


import com.alibaba.fastjson.TypeReference;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.srz.common.constant.ProductConstant;
import com.srz.common.to.MemberPrice;
import com.srz.common.to.SkuHasStockVo;
import com.srz.common.to.SkuReductionTo;
import com.srz.common.to.SpuBoundTo;
import com.srz.common.to.es.SkuEsModel;
import com.srz.common.utils.R;
import com.srz.gulimall.product.entity.*;
import com.srz.gulimall.product.feign.CouponFeignService;
import com.srz.gulimall.product.feign.SearchFeignService;
import com.srz.gulimall.product.feign.WareFeignService;
import com.srz.gulimall.product.service.*;
import com.srz.gulimall.product.vo.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.product.dao.PmsSpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


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

    @Autowired
    PmsBrandService brandService;

    @Autowired
    PmsCategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                new QueryWrapper<PmsSpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //TODO 高级部分再来完善
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
                }).filter(entity->{
                    //返回True则收集
                    return StringUtils.hasText(entity.getImgUrl());
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

                String gson = new Gson().toJson(item.getMemberPrice());
                memberPriceList = new Gson().fromJson(gson, new TypeToken<List<MemberPrice>>() {
                }.getType());

/*                item.getMemberPrice().forEach(memberPrice -> {
                    com.srz.common.to.MemberPrice price = new com.srz.common.to.MemberPrice();
                    price.setId(memberPrice.getId());
                    price.setName(memberPrice.getName());
                    price.setPrice(memberPrice.getPrice());
                    memberPriceList.add(price);
                });*/


                skuReductionTo.setMemberPrice(memberPriceList);

                if (skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r2 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r2.getCode() != 0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }

            });
        }

    }

    @Override
    public void  saveBaseSpuInfo(PmsSpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        QueryWrapper<PmsSpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.hasText(status)){
            wrapper.eq("publish_status",status);

        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.hasText(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);


        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasText(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }


        IPage<PmsSpuInfoEntity> page = this.page(
                new Query<PmsSpuInfoEntity>().getPage(params),
                wrapper

        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        //1、查出当前skuid对应的所有sku信息，品牌的名字
        List<PmsSkuInfoEntity> skus = skuInfoService.getSpusBySpuID(spuId);
        List<Long> skuIdList = skus.stream().map(PmsSkuInfoEntity::getSpuId).collect(Collectors.toList());

        //TODO 4、查询当前sku的所有可以被检索规格属性
        List<PmsProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrsIds = attrService.selectSearchAttrIds(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrsIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        //TODO 1、发送远程调用，库存系统查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try{
            R skusHasStock = wareFeignService.getSkusHasStock(skuIdList);
            //
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            stockMap = skusHasStock.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        } catch (Exception e){
            log.error("库存服务查询异常：原因{}",e);
        }


        //2、封装每个sku的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);
//          skuPrice,skuImg,
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
//          hasStock,hotScore,
            if(finalStockMap == null){
                esModel.setHasStock(true);
            } else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //TODO 2、热度评分 0
            esModel.setHotScore(0L);

            //TODO 3、查询品牌和分类的名字信息
//          brandName;brandImg;catalogName;
            PmsBrandEntity brandEntity = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());
            PmsCategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(category.getName());

            //设置检索属性
            esModel.setAttr(attrsList);


            return esModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发送给es进行保存;gulimall-search
         R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode() == 0) {
            //远程调用成功
            //TODO 6、修改当前spu的状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());

        } else {
            //远程调用失败
            //TODO 7、重复调用？接口幂等性？重试机制？
            //feign调用流程
            /*
            * 1、构造请求数据，将对象转为json
            *   RequestTemplate template = buildTemplateFromArgs.create(argv);
             * 2、发送请求进行执行(执行成功会解码响应数据)
             *  executeAndDecode(template);
             *
             * 3、执行请求会有重试机制
             *  while(true){
             *      try{
             *          executeAndDecode(template);
             *      } catch(){
             *          try{
             *              retryer.continueOrPropagate(e);
             *          } catch(){
             *               throw ex;
             *          }
             *          continue;
             *      }
             *
             *  }
             *
             *
            * */
        }



//        private List<Object> attr;
//        @Data
//        public static class Attrs{
//            private Long attrId;
//            private String attrName;
//            private String attrValue;
//        }


    }


}