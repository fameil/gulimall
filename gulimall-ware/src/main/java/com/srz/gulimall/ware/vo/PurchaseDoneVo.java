package com.srz.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author srz
 * @create 2022/12/2 23:34
 */

@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id; //采购单id

    private List<PurchaseItemDoneVo> items;


}
