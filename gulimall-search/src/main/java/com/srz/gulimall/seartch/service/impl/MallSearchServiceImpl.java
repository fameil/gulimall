package com.srz.gulimall.seartch.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.srz.common.to.es.SkuEsModel;
import com.srz.common.utils.R;
import com.srz.gulimall.seartch.config.GulimallElasticSearchConfig;
import com.srz.gulimall.seartch.constant.EsConstant;
import com.srz.gulimall.seartch.feign.ProductFeignService;
import com.srz.gulimall.seartch.service.MallSearchService;
import com.srz.gulimall.seartch.vo.AttrResponseVo;
import com.srz.gulimall.seartch.vo.BrandVo;
import com.srz.gulimall.seartch.vo.SearchParam;
import com.srz.gulimall.seartch.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.util.AggregateUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author srz
 * @create 2023/2/1 10:56
 */

@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    ProductFeignService productFeignService;

    //去es进行检索
    @Override
    public SearchResult search(SearchParam parm){
        //1、构建出查询需要的DSL语句
        SearchResult result = null;

        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequrest(parm);

        try {
            //2、执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

            //3、分析响应数据封装成我们需要的格式
            result = buildSearchResult(response,parm);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 准备检索请求
     * 模糊匹配，过滤，（按照属性，品牌，价格区间，库存）高亮，聚合分析
     * #如果是 嵌入式的属性查询，取保，分析都应该用嵌入式的方式
     * @return
     */
    private SearchRequest buildSearchRequrest(SearchParam parm) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//构建DSL语句



        /**
         * 查询：模糊匹配，过滤（按照属性，分头，品牌，价格区间，库存）
         */
        //1、构建bool  - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1、must-模糊匹配
        if(!StringUtils.isEmpty(parm.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",parm.getKeyword()));
        }
        //1.2、bool - filter - 按照三级分类id查询
        if(parm.getCatalog3Id()!=null){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",parm.getCatalog3Id()));
        }
        //1.2、bool - filter - 按照品牌id查询
        if(parm.getBrandId()!=null && parm.getBrandId().size()>0){
            boolQuery.filter(QueryBuilders.termsQuery("brandId",parm.getBrandId()));
        }
        //1.2、bool - filter - 按照品牌id查询 - 按照所有指定的属性进行查询
        if(parm.getAttrs()!=null && parm.getAttrs().size()>0){
            for (String attrStr : parm.getAttrs()) {
                //attrs=1_5寸:8寸&attrs=2_16G:8G
                BoolQueryBuilder nestedboolQuery = QueryBuilders.boolQuery();
                //1_5寸:8寸
                String[] s = attrStr.split("_");
                String attrId = s[0];//检索的属性id
                String[] attrValues = s[1].split(":");//这个属性的检索用的值
                nestedboolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedboolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrValues));
                //每一个必须都得生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedboolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);

            }

        }

        //1.2、bool - filter - 按照库存是否有进行查询
        if (parm.getHasStock() != null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock",parm.getHasStock()==1));
        }
        //1.2、bool - filter - 按照价格区间查询
        if(!StringUtils.isEmpty(parm.getSkuPrice())){
            //1_500/_500/500_
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = parm.getSkuPrice().split("_");
            // "_500".split("_").length = 2
            if (StringUtils.isEmpty(s) && parm.getSkuPrice().startsWith("_")){
                rangeQuery.lte(s[1]);
            } else if (s.length == 2){
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length ==1){
                rangeQuery.gte(s[0]);
            }

            boolQuery.filter(rangeQuery);
        }

        //接以前的所有条件都拿来进行封装
        sourceBuilder.query(boolQuery);

        /**
         * 排序，分页，高亮，
         */
        //2.1、排序
        if(!StringUtils.isEmpty(parm.getSort())){
            String sort = parm.getSort();
            //sort=hotScore_asc
            String[] s = sort.split("_");
            SortOrder order = SortOrder.fromString(s[1]);
            sourceBuilder.sort(s[0], order);
        }
        //2.2、分页
        sourceBuilder.from((parm.getPageNum()-1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE );

        //2.2、高亮
        if (!StringUtils.isEmpty(parm.getKeyword())){
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");

            sourceBuilder.highlighter(builder);
        }

        /**
         * 聚合分析
         */
        //1、品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);

        // 品牌聚合的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        //1聚合brand
        sourceBuilder.aggregation(brand_agg);

        //2、分类聚合 catalog_agg
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        //1聚合catalog
        sourceBuilder.aggregation(catalog_agg);

        //3、属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        //聚合分析当前attr_id对应的名字
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析当前attr_id对应的所有可能的属性值attrValue
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));

        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);

        String s = sourceBuilder.toString();
        System.out.println(s);


        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }
    /**
     * 构建结果数据
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response,SearchParam parm) {


        SearchResult result = new SearchResult();
        //1、返回的所有查询的商品
        SearchHits hits = response.getHits();

        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits()!=null && hits.getHits().length>0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString,SkuEsModel.class);
                if (!StringUtils.isEmpty(parm.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);

//        //2、当前所有商品涉及到的所有属性信息
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1、得到属性的id
            long attrId = bucket.getKeyAsNumber().longValue();
            //2、得到属性的名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            //3、得到属性的所有值
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> {
                String keyAsString = ((Terms.Bucket) item).getKeyAsString();

                return keyAsString;
            }).collect(Collectors.toList());

            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);


            attrVos.add(attrVo);
        }
        

        result.setAttrs(attrVos);
//        //3、当前所有商品涉及到的所有品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //1、得到品牌的id
            long brandId = bucket.getKeyAsNumber().longValue();
            //2、得到品牌的名字
            String brandNameAgg = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //3、得到品牌的图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandNameAgg);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);

        }

        result.setBrands(brandVos);
//        //4、当前所有商品涉及到的所有分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");

        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //得到分类id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            //得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
//        ===========以上从聚合信息中获取==============



//        //5、分页信息-页码
        result.setPageNum(parm.getPageNum());
//        //6、分页信息-总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
//        //7、分页信息-总页码--计算得到11/2 = 5 .. 1
        int totalPages = (int)total%EsConstant.PRODUCT_PAGESIZE == 0 ? (int)total/EsConstant.PRODUCT_PAGESIZE : ((int)total/EsConstant.PRODUCT_PAGESIZE+1);

        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for(int i = 1; i <= totalPages; i++){
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);


        //8、面包屑导航功能
        if (parm.getAttrs()!=null && parm.getAttrs().size()>0){
            List<SearchResult.NavVo> collect = parm.getAttrs().stream().map(attr -> {
                //1、分析每个attr传过来的查询参数值
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                //attrs=2_5寸:6寸
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if(r.getCode()==0){
                    AttrResponseVo data = r.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                }else {
                    navVo.setNavName(s[0]);
                }
                //2、取消了这个面包屑以后，我们要跳转到哪个地方，将请求地址的url里面的当前置空
                //拿到所有的查询条件，去掉当前.
                //attrs=11_麒麟990系列
                String replace = replaceQueryString(parm, attr,"attrs");

                navVo.setLink("http://search.gulimall.com/list.html?"+replace);

                return navVo;
            }).collect(Collectors.toList());

            result.setNavs(collect);
        }

        //品牌，分类
        if (parm.getBrandId()!=null && parm.getBrandId().size()>0){
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();

            navVo.setNavName("品牌");
            //TODO 远程查询所有品牌
            R r = productFeignService.brandsInfo(parm.getBrandId());
            if (r.getCode()==0) {
                List<BrandVo> brand = r.getData("pmsBrand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getBrandName()+";");
                    replace = replaceQueryString(parm, brandVo.getBrandId()+"","brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.gulimall.com/list.html?"+replace);
            }

            navs.add(navVo);

        }
        //TODO 分类，不需要导航取消



        return result;
    }

    private static String replaceQueryString(SearchParam parm, String value,String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            //浏览对空格编码差异化处理跟java不一样
            encode = encode.replace("+","%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String replace = parm.get_queryString().replace("&"+key+"="+encode, "");
        return replace;
    }
}
