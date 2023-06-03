package com.srz.gulimall.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author srz
 * @create 2023/4/15 10:13
 */
@ToString
@Data
public class SocialUser {
    private String id;
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String scope;
    private long created_at;
}
