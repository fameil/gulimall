package com.srz.gulimall.service;

import com.srz.gulimall.vo.Cart;
import com.srz.gulimall.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author srz
 * @create 2023/5/5 7:32
 */
public interface CartService {
    //将商品再加到购物车
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    //获购物车中的某个购物项
    CartItem getCartItem(Long skuId);

    /**
     * 获取整个购物车
     * @return
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车数据
     * @param cartKey
     */
    public void clearCart(String cartKey);

    /**
     * 勾选购物挥
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除购物项
     * @param skuId
     */
    void deleteItem(Long skuId);

    List<CartItem> getUserCartItems();
}
