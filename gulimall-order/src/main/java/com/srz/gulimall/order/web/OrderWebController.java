package com.srz.gulimall.order.web;

import com.srz.gulimall.order.service.OrderService;
import com.srz.gulimall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @author srz
 * @create 2023/5/21 23:25
 */
@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    @GetMapping("toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();


        model.addAttribute("orderConfirmData",confirmVo);

      //展示订单确认的数据
      return "confirm";
    }


}
