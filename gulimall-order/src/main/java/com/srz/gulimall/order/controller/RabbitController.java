package com.srz.gulimall.order.controller;

import com.srz.gulimall.order.entity.OrderEntity;
import com.srz.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author srz
 * @create 2023/5/15 12:59
 */

@RestController
public class RabbitController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMq/{num}")
    public String sendMq(@PathVariable(value = "num") Integer num) {
        for (int i = 0; i < num; i++) {
            if(i%2 == 0){
                OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
                reasonEntity.setId(3L);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("李四-"+i);
                //1、发送消息,如果发送的举息是个对象，需要使用序列化机制
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java", reasonEntity,new CorrelationData(UUID.randomUUID().toString()));

            } else {
//                OrderEntity entity = new OrderEntity();
//                entity.setOrderSn(UUID.randomUUID().toString());
//                rabbitTemplate.convertAndSend("hello-java-exchange","hello22.java", entity,new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }


}
