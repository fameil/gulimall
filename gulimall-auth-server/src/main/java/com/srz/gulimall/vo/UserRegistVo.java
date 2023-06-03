package com.srz.gulimall.vo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


/**
 * @author srz
 * @create 2023/4/2 16:36
 */
@Data
public class UserRegistVo {

    @NotEmpty(message = "用户名必须提交")
    @Length(min=6,max=18,message="用户名必须是6-18位字符")
    @Pattern(regexp = "^(?![0-9]+$)[a-zA-Z0-9_]{4,16}$", message = "用户名不能为纯数字")
    private String userName;

    @NotEmpty(message = "密码必须填写")
    @Length(min=6,max=18,message="密码必须是6-18位字符")
    private String password;

    @NotEmpty(message = "手机号必须填写")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码必须填写")
    private String code;

}
