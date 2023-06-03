package com.srz.gulimall.member.exception;

/**
 * @author srz
 * @create 2023/4/9 7:50
 */
public class PhoneExsitException extends RuntimeException {
    public PhoneExsitException() {
        super("手机号已存在");
    }
}
