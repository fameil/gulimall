package com.srz.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.srz.common.utils.HttpUtils;
import com.srz.gulimall.member.dao.MemberLevelDao;
import com.srz.gulimall.member.entity.MemberLevelEntity;
import com.srz.gulimall.member.exception.PhoneExsitException;
import com.srz.gulimall.member.exception.UsernameExisException;
import com.srz.gulimall.member.vo.MemberLoginVo;
import com.srz.gulimall.member.vo.MemberRegistVo;
import com.srz.gulimall.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.member.dao.MemberDao;
import com.srz.gulimall.member.entity.MemberEntity;
import com.srz.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo){
        MemberEntity entity = new MemberEntity();
        MemberDao memberDao = this.baseMapper;

        //设置默认等级
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());

        //检查用户名和手机号是否唯一，为了让controller能感知异常，使用异常机制
        checkPhoneUniques(vo.getPhone());
        checkUsernameUniques(vo.getUserName());

        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        entity.setNickname(vo.getUserName());

        //密码要进行加密存储。
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode );

        //其他的默认信息
        memberDao.insert(entity);
    }

    @Override
    public void checkPhoneUniques(String phone) throws PhoneExsitException {
        MemberDao memberDao = this.baseMapper;
        Long mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0 ){
            throw new PhoneExsitException();
        }
    }

    @Override
    public void checkUsernameUniques(String username) throws UsernameExisException  {
        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0 ){
            throw new UsernameExisException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {

        //1、去数据库查询
        MemberEntity entity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", vo.getLoginacct())
                .or().eq("mobile", vo.getLoginacct()));
        if (entity == null){
            //登录失败
            return null;
        } else {
            //1、获取到数据库的password   $2a$10$7aSApkTeAtmmek16vZxH5uKbfpZDfFDrnxAqiuLZpBg0esF4IiOU2
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2、密码匹配
            boolean matches = passwordEncoder.matches(vo.getPassword(), entity.getPassword());
            if (matches){
                return entity;
            } else {
                return null;
            }
        }
    }

    @Override
    public MemberEntity login(SocialUser socialUser) {
        //登录和注册合并逻辑
        String uid = socialUser.getId();
        MemberDao memberDao = this.baseMapper;
        MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if (memberEntity != null){
            //这个用户已经注册
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());

            memberDao.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());

            return memberEntity;
        } else {
            //2、没有查到当前社交用户对应的记录我们就需要注册
            MemberEntity regist = new MemberEntity();
            try{
                Map<String,String> header = new HashMap<>();
                Map<String,String> query = new HashMap<>();
                query.put("access_token",socialUser.getAccess_token());
                HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", header,query);
                if (response.getStatusLine().getStatusCode()==200) {
                    //查询成功
                    String userInfoJson = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(userInfoJson);

                    //呢称
                    String name = jsonObject.getString("name");
                    regist.setNickname(name);
                }

            }catch (Exception e){}

            regist.setSocialUid(socialUser.getId());
            regist.setAccessToken(socialUser.getAccess_token());
            regist.setExpiresIn(socialUser.getExpires_in());

            memberDao.insert(regist);

            return regist;

        }

    }

}