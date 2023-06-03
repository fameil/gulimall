package com.srz.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.srz.common.utils.PageUtils;
import com.srz.gulimall.member.entity.MemberEntity;
import com.srz.gulimall.member.exception.PhoneExsitException;
import com.srz.gulimall.member.exception.UsernameExisException;
import com.srz.gulimall.member.vo.MemberLoginVo;
import com.srz.gulimall.member.vo.MemberRegistVo;
import com.srz.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author srz
 * @email sunlightcs@gmail.com
 * @date 2022-09-08 23:37:59
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUniques(String phone) throws PhoneExsitException;
    void checkUsernameUniques(String username) throws UsernameExisException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

