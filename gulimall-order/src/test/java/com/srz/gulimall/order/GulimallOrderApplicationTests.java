package com.srz.gulimall.order;

import com.alibaba.fastjson.JSON;
import com.srz.gulimall.order.entity.OrderEntity;
import com.srz.gulimall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

/**
 * @author srz
 * @create 2023/5/14 16:29
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessageTest(){
        for (int i = 0; i < 10; i++) {
            if(i%2 == 0){
                OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
                reasonEntity.setId(3L);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("李四-"+i);
                //1、发送消息,如果发送的举息是个对象，需要使用序列化机制
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java", reasonEntity);

            } else {
                OrderEntity entity = new OrderEntity();
                entity.setOrderSn(UUID.randomUUID().toString());
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java", entity);
            }
            System.out.println("消息发送完成"+i);
        }
    }

    /**
     * 1、如何创建Exchange[hello-java-exchange]、Queue、Binding
     *      1.1 使用AmqpAdmin进行创建
     * 2、如何收发消息
     *
     */
     @Test
     public void createExchange(){
         //Exchange
         //DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
         DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
         amqpAdmin.declareExchange(directExchange);
         log.info("Exchange[hello-java-exchange]创建成功");

     }

     @Test
     public void createQueue(){
         //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
         Queue queue = new Queue("hello-java-queue",true,false,false);
         amqpAdmin.declareQueue(queue);
         log.info("Queue创建成功hello-java-queue");
     }

     @Test
     public void createBinding(){
         //Binding(String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments)
         //                目的地,目的地类型,交换机,路由键, 自定义参数
         Binding binding = new Binding(
                 "hello-java-queue",
                 Binding.DestinationType.QUEUE,
                 "hello-java-exchange",
                 "hello.java",
                 null);
         amqpAdmin.declareBinding(binding);
         log.info("Binding创建成功");
     }
}
