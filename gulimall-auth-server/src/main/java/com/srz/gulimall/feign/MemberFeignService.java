package com.srz.gulimall.feign;

import com.srz.common.utils.R;
import com.srz.gulimall.vo.SocialUser;
import com.srz.gulimall.vo.UserLoginVo;
import com.srz.gulimall.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author srz
 * @create 2023/4/10 0:29
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegistVo vo);


    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R oauthlogin(@RequestBody SocialUser socialUser) throws Exception;
}
