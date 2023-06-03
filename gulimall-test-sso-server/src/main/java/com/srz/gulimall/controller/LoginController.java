package com.srz.gulimall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author srz
 * @create 2023/4/29 0:09
 */
@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("token") String token){
        String s = redisTemplate.opsForValue().get(token);
        return s;
    }

    @GetMapping("/login.html")
    public String loginPages(@RequestParam("redirect_url") String url, Model model,
                             @CookieValue(value = "sso_token",required = false)String sso_token){
        if (!StringUtils.isEmpty(sso_token)){
            //登录过
            return "redirect:"+url+"?token="+sso_token;
        }
        model.addAttribute("url",url);

        return "login";
    }


    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username")String username,
                          @RequestParam("password")String password,
                          @RequestParam("url")String url,
                          HttpServletResponse response){

        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){

            String uuid = UUID.randomUUID().toString().replace("-","");
            redisTemplate.opsForValue().set(uuid,username,30, TimeUnit.SECONDS);
            Cookie sso_token = new Cookie("sso_token", uuid);
            response.addCookie(sso_token);
            //登录成功,跳回之前页面
            return "redirect:"+url+"?token="+uuid;

        }
        //登录失败
        return "login";
    }

}
