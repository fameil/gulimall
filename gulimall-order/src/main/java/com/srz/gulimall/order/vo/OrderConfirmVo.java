package com.srz.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author srz
 * @create 2023/5/22 1:05
 */
//订单确认页需要用的数据
@Data
public class OrderConfirmVo {
    //收货地址 ums_member_receive_address
    List<MemberAddressVo> address;

    //所有选中的购物项
    List<OrderItemVo> items;

    //发票记录...

    //优惠券信息
    Integer integration;

    //订单防重令牌
    String orderToken;

    //BigDecimal total;//订单总额

    public BigDecimal getTotal(){
        //订单总额
        BigDecimal sum = new BigDecimal("0");
        if (items!= null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum = sum.add(multiply);
            }
        }
        return sum;
    }

//    BigDecimal payPrice;

    //应付价格
    public BigDecimal getPayPrice() {
        return getTotal();
    }




}
