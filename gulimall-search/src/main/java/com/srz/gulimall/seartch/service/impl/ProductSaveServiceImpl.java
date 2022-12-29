package com.srz.gulimall.seartch.service.impl;

import com.alibaba.fastjson2.JSON;
import com.srz.common.to.es.SkuEsModel;
import com.srz.gulimall.seartch.config.GulimallElasticSearchConfig;
import com.srz.gulimall.seartch.constant.EsConstant;
import com.srz.gulimall.seartch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author srz
 * @create 2022/12/23 0:16
 */

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {


    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModes) throws IOException {

        //保存到es
        //1、给es中建立索引product，建立好映射关系
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsMode : skuEsModes) {
            //1、 构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsMode.getSkuId().toString());
            String s = JSON.toJSONString(skuEsMode);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);

        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //TODO 1、批量是否错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        log.info("商品上架完成:{},返回数据：{}",collect,bulk.toString());

        return b;

    }
}
