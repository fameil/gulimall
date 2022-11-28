package com.srz.gulimall.thirdparty.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.srz.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author srz
 * @create 2022/10/26 20:38
 */
@RestController
public class OssController {
    @Value("${accesskey}")
    private String accessKey;

    @Value("${secretkey}")
    private String secretKey;

    @Value("${bucket}")
    private String bucket;

    @Value("${qiniuhost}")
    private String qiniuhost;

    @RequestMapping("/oss/policy")
    public R policy(){
        System.out.println(accessKey);
        System.out.println(secretKey);
        System.out.println(bucket);

        //构造一个带指定 Region 对象的配置类	Region.region2(), Region.huanan()
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

//        String accessKey = "optFqKHdw0PdmRdW1Eo_OaH1xqfQxmpg7CgppDPQ";
        //String secretKey = "KFl3lTqXTn-ZqaHCUPi3af-gjeTinfpmlXvhrXoM";

        //String bucket = "gulimall-srz";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        ///String localFilePath = "C:\\Users\\oWo\\Desktop\\69618f6fly1h7n2oz3ykmj206y06y74t.jpg";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String name = "123/69618f6fly1h7n2oz3ykmj206y06y74t.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String dir = LocalDate.now().toString()+"/";
        System.out.println(dir);

        Map<String,String> respMap = new LinkedHashMap<>();
        respMap.put("policy", upToken);
        respMap.put("dir", dir);

        respMap.put("host", "http://"+qiniuhost);


//        try {
//            Response response = uploadManager.put(localFilePath, name, upToken);
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
//        System.out.println("上传完成!");

        return  R.ok().put("data",respMap);
    }


}
