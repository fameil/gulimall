package com.srz.gulimall.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author srz
 * @create 2023/5/14 22:00
 */
@Configuration
public class MyRabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //使用JSON序列化机制，进行消息转换
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //定制RabbitTempte
    // 1、服务收到消息就回调
    //      1、spring.rabbitmq.publisher-confirm-type=correlated
    //      2、设置确认回调ConfirmCallback
    // 2、消息正确抵达队列进行回调
    //      1、spring.rabbitmq.publisher-returns=true
    //         spring.rabbitmq.template.mandatory=true
    // 3、消费端确认（保证每个被正确消费，此时才可以broker删除这个消息）。
//           #手动ack消息
//            spring.rabbitmq.listener.simple.acknowledge-mode=manual
    //      1、默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
    //         没有确认消息就一直是unacked状态。即使Consumer宕机。消息不会丢失，会重新变为Ready
//            channel.basicAck(deliveryTag,false);//签收消息
//            channel.basicNack(deliveryTag,false,true);//拒收消息
    //
    @PostConstruct  //MyRabbitConfig对象创建完成以后，执行这个方法
    public void initRabbitTemplate(){
        //设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 1、只要抵达Broker就ack=true
             * @param correlationData 当前消息的唯一关联数据（是消息的唯一id）
             * @param b 消息是否成功收到ack
             * @param s 失败的原因cause
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("消息回调confirm.correlationData=["+correlationData+"]ack：["+b+"]cause：["+s+"]");
            }
        });
        //设置消息抵达队列的确认回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            //只要沙影没有投递给指定的队列，就触发这个失败回调
//             message    投递失败的详细信息
//             replyCode  回复的状态码
//             replyText  回复的文本内容
//             exchange   发给哪个交换机
//             routingKey 用哪个路由键
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println(returnedMessage);
            }
        });
    }

}
