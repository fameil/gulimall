package com.srz.gulimall.member.exception;

/**
 * @author srz
 * @create 2023/4/9 7:49
 */
public class UsernameExisException extends RuntimeException {
    public UsernameExisException() {
        super("用户名已存在");
    }
}
