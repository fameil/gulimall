package com.srz.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author srz
 * @create 2022/11/27 19:54
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;


}
