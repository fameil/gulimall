package com.srz.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author srz
 * @create 2022/12/1 22:45
 */

@Data
public class MergeVo {

//    items:[1, 2]
//    purchaseId:1
    private Long purchaseId;//整单ID
    private List<Long> items;//合并项集合[1, 2]


}