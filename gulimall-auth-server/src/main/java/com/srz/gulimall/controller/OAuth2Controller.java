package com.srz.gulimall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.srz.common.utils.HttpUtils;
import com.srz.common.utils.R;
import com.srz.gulimall.feign.MemberFeignService;
import com.srz.common.vo.MemberRespVo;
import com.srz.gulimall.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author srz
 * @create 2023/4/15 8:47
 * 处理社交登录请求
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/oauth2/gitee/success")
    public String gitee(@RequestParam("code") String code, HttpSession session ) throws Exception {
        Map<String,String> header = new HashMap<>();
        Map<String,String> query = new HashMap<>();
        Map<String,String> map = new HashMap<>();


        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("client_id","21efce284a97e32f11e59da74b0542412149a4e3cf49d8a911e1b60f7d4e6a2d");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2/gitee/success");
        map.put("client_secret","f87e1f6fede59c9b2fd0d974695c72639f44e56498238d51b8251521f216d632");
        //1、根据code换取access_token
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", header, query, map);
        //2、处理
        if(response.getStatusLine().getStatusCode()==200){

            //获取到了accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //获取用户UID
            Map<String,String> query2 = new HashMap<>();
            query2.put("access_token",socialUser.getAccess_token());
            HttpResponse responseUser = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<String,String>(),query2);
            if (responseUser.getStatusLine().getStatusCode()==200) {
                String userInfoJson = EntityUtils.toString(responseUser.getEntity());
                //GiteeUserInfo userInfo = JSON.parseObject(userInfoJson, GiteeUserInfo.class);
                JSONObject jsonObject = JSON.parseObject(userInfoJson);
                socialUser.setId(jsonObject.getString("id"));
                //知道当前是哪个社交用户
                //1、当前用户如果是第一次进网站，自动注册
                //为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员
                //登录或注册这个社交用户
                R oauthlogin = memberFeignService.oauthlogin(socialUser);
                if (oauthlogin.getCode()==0) {
                    MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                    });
                    log.info("登录成功，用户信息:{}"+data.toString());
                    //TODO 1、默认发令牌，作用域：当前域
                    //TODO 2、使用JSON序列化方式
                    session.setAttribute("loginUser",data);
                    //2、登录成功就跳回首页
                    return "redirect:http://gulimall.com";
                }else {
                    return "redirect:http://auth.gulimall.com/login.html";
                }
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
