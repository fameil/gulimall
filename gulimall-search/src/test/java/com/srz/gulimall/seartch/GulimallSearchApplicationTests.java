package com.srz.gulimall.seartch;

import com.alibaba.fastjson.JSON;
import com.srz.gulimall.seartch.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author srz
 * @create 2022/12/9 21:43
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    private RestHighLevelClient client;

    @ToString
    @Data
    static class Accout {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    @Test
    public void searchData() throws IOException {
        //1?????????????????????
        SearchRequest searchRequest = new SearchRequest();
        //????????????
        searchRequest.indices("bank");
        //??????DSL???????????????
        //SearchSourceBuilder sourceBuilder ????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query();
//        sourceBuilder.from();
//        sourceBuilder.size();
//        sourceBuilder.aggregation();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        //1.2 ????????????????????????????????????
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        //1.3 ??????????????????
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg3").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println("???????????????"+sourceBuilder.toString());

        searchRequest.source(sourceBuilder);

        //2???????????????
        SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //3???????????????
        System.out.println(searchResponse.toString());
        //Map map = JSON.parseObject(searchRequest.toString(), Map.class);

        //???????????????????????????
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHists = hits.getHits();
        for (SearchHit hit : searchHists) {
            //????????????????????????
//            hit.getIndex();hit.getType();hit.getId();
            String sourceAsString = hit.getSourceAsString();
            Accout accout = JSON.parseObject(sourceAsString, Accout.class);
            System.out.println(accout.toString());

         }

        //3.2 ????????????????????????????????????
        Aggregations aggregations = searchResponse.getAggregations();
//        for (Aggregation aggregation : aggregations.asList()) {
//            System.out.println("???????????????"+aggregation.getName());
//        }
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("?????????"+keyAsString);
        }
        Avg balanceAvg3 = aggregations.get("balanceAvg3");
        System.out.println("???????????????"+balanceAvg3.getValue());

    }

    //??????????????????
    //??????\???????????????
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");//?????????ID
        //indexRequest.source("userName","zhangsan","age",18,"gender","???");
        User user = new User();
        user.setUserName("??????");
        user.setAge(18);
        user.setGender("???");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);//??????????????????

        //????????????
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //???????????????????????????
        System.out.println(index);

    }

    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }


    @Test
    public void contextLoads(){
        System.out.println(client);
    }
}
