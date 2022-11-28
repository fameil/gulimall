package com.srz.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author srz
 * @create 2022/11/27 16:58
 */
@Data
public class SpuBoundTo {

    private Long spuId;

    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
