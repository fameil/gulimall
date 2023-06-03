package com.srz.gulimall.controller;

import com.alibaba.fastjson.TypeReference;
import com.srz.common.constant.AuthServerConstant;
import com.srz.common.exception.BizCodeEnume;
import com.srz.common.utils.R;
import com.srz.common.vo.MemberRespVo;
import com.srz.gulimall.feign.MemberFeignService;
import com.srz.gulimall.feign.ThirdPartFeignService;
import com.srz.gulimall.vo.UserLoginVo;
import com.srz.gulimall.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author srz
 * @create 2023/3/26 22:10
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone){
        //TODO 1、接口防刷

        String rediscode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(rediscode)){
            long time = Long.parseLong(rediscode.split("_")[1]);
            if (System.currentTimeMillis() - time < 60000) {
                //接口防刷60秒内不能再次发送
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        int randomcode = new Random().nextInt(89999) + 10000;
        String code = randomcode+"_"+System.currentTimeMillis();
        //验证校验、redis.存key-phone,value-code
        // sms:code:136****7905
        //缓存验证码 void set(K key, V value, long timeout, TimeUnit unit);
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,code,10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,randomcode+"");
        return R.ok();
    }

    //TODO 重定向携带数据，利用session原理。 将数据放在session中。只要跳到下一个页面取出这个数据以后，session里 面的数据就会删掉
    //TODO 1、分布式下的session问题。|
    //RedirectAttributes redirectAttributes: 模拟重定向携带数据
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {

            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            //model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错
            //Request method 'POST' not supported
            //用户注册->/regist[post----》转发/reg. html (路径映射默认都是get方式访问的。)
            //return "redirect:/login.html";
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //真正注册。调用远程服务进行
        //1、校验验证码
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equals(s.split("_")[0])){
                //删除验证码;令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证通过。//真正注册。调用远程服务进行注册
                R r = memberFeignService.regist(vo);
                if (r.getCode()==0){
                    //成功
                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }

            }else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                //model.addAttribute("errors",errors);
                redirectAttributes.addFlashAttribute("errors",errors);
                //校验出错，转发到注册页
                return "redirect:http://auth.gulimall.com/reg.html";
            }

        }else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            //model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute == null){
            //没登录
            return "login";
        } else {
            return "redirect:http://gulimall.com";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redierctAttributes, HttpSession session){

        //远程登录
        R login = memberFeignService.login(vo);
        if(login.getCode()==0){
            //成功
            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://gulimall.com";
        } else {
            Map<String,String> errors = new HashMap<>();
            errors.put("msg", login.getData("msg",new TypeReference<String>(){}));
            redierctAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }




}
