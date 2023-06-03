package com.srz.gulimall.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author srz
 * @create 2023/5/9 6:23
 */
@ToString
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean tempUser = false;
}
