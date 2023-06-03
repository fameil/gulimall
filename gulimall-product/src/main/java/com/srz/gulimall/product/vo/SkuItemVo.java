package com.srz.gulimall.product.vo;

import com.srz.gulimall.product.entity.PmsSkuImagesEntity;
import com.srz.gulimall.product.entity.PmsSkuInfoEntity;
import com.srz.gulimall.product.entity.PmsSpuInfoDescEntity;
import com.srz.gulimall.product.entity.PmsSpuInfoEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author srz
 * @create 2023/3/10 4:56
 */

@Data
public class SkuItemVo {
    //1、sku基本信息获取 pms_sku_info
    PmsSkuInfoEntity info;

    boolean hasStock = true;

    //2、sku的图片信息 pms_sku_images
    List<PmsSkuImagesEntity> images;

    //3、获取spu的销售属性组合
    List<SkuItemSaleAttrsVo> saleAttr;

    //4、获取spu的介绍
    PmsSpuInfoDescEntity desc;

    //5、获取spu规格参数信息
    List<SputItemAttrGroupVo> groupAttrs;

    @ToString
    @Data
    public static class SkuItemSaleAttrsVo{
        /**属性id*/
        private Long attrId;
        /**属性名*/
        private String attrName;
        //对应的所有值
        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @ToString
    @Data
    public static class SputItemAttrGroupVo{
        private String groutName;
        private List<SpuBaseAttrVo> attrs;
    }

    @ToString
    @Data
    public static class SpuBaseAttrVo{
        /**属性名*/
        private String attrName;
        //对应的所有值
        private String attrValue;
    }

}
