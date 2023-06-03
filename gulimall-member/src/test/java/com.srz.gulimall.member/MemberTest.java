package com.srz.gulimall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author srz
 * @create 2023/4/9 8:22
 */
public class MemberTest {
    @Test
    public void contextLoads() {

        //MD5   彩虹表
        //e10adc3949ba59abbe56e057f20f883e
        String s = DigestUtils.md5Hex("123456");

        //盐值加密
        //$1$qqqqqqqq$AZofg3QwurbxV3KEOzwuI1   123456
        String s1 = Md5Crypt.md5Crypt("123456".getBytes(),"$1$qqqqqqqq");

        //$2a$10$1L/EIzpcgSVqfsP1pqHbhe0VGHTzVxDVI5W/6cNw/k2RAJxkq5X6C
        //$2a$10$d/q24dbLoFgqjEXGTc3Iy.NpyQufofNRhGCqkpqMRSU3zMx0Y/KQS
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");

        boolean matches = passwordEncoder.matches("123456", "$2a$10$d/q24dbLoFgqjEXGTc3Iy.NpyQufofNRhGCqkpqMRSU3zMx0Y/KQS");

        System.out.println(matches);

    }
}
