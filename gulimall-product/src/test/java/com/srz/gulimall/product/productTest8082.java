package com.srz.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.srz.gulimall.product.entity.PmsBrandEntity;
import com.srz.gulimall.product.service.PmsBrandService;
import com.srz.gulimall.product.service.PmsCategoryService;
import com.srz.gulimall.product.utils.Qiniu;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author srz
 * @create 2022/9/8 16:23
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class productTest8082 {
    @Autowired
    Qiniu qiniu;

    @Autowired
    PmsBrandService brandService;

    @Autowired
    PmsCategoryService categoryService;

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225l);
        log.info("完整路径:{}", Arrays.asList(catelogPath));


    }

    @Test
    public void TestFile2() throws IOException {
        qiniu.qiniuOss("C:\\Users\\oWo\\Desktop\\练习\\001-2022-9-27.png"
        ,"001-2022-9-27.png");
    }
//    @Test
//    public void TestFile() throws IOException {
//
//        //构造一个带指定 Region 对象的配置类	Region.region2(), Region.huanan()
//        Configuration cfg = new Configuration(Region.region2());
//        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//        //...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
//        //...生成上传凭证，然后准备上传
//        String accessKey = "optFqKHdw0PdmRdW1Eo_OaH1xqfQxmpg7CgppDPQ";
//        String secretKey = "KFl3lTqXTn-ZqaHCUPi3af-gjeTinfpmlXvhrXoM";
//        String bucket = "gulimall-srz";
//        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "C:\\Users\\oWo\\Desktop\\006ARE9vgy1fwxok61mmtj30r30qo777.jpg";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = "006ARE9vgy1fwxok61mmtj30r30qo777.jpg";
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(localFilePath, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//        System.out.println("上传完成。。。。");
//    }

    @Test
    public void Test1() throws IOException {
        PmsBrandEntity brandEntity = new PmsBrandEntity();
/*       //测试保存
        brandEntity.setName("华为");
        brandService.save(brandEntity);*/
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("测试修改");
        brandService.updateById(brandEntity);
        List<PmsBrandEntity> list = brandService.list(new QueryWrapper<PmsBrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->{
            System.out.println(item);
        });


        System.out.println("====================");;
        System.out.println(brandEntity.toString());
    }

}



