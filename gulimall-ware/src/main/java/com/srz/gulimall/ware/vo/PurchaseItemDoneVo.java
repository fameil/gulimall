package com.srz.gulimall.ware.vo;

import lombok.Data;

/**
 * @author srz
 * @create 2022/12/2 23:36
 */
@Data
public class PurchaseItemDoneVo {

    //{itemId:2,status:4,reason:"无货"}
    private Long itemId;
    private Integer status;
    private String reason;

}
