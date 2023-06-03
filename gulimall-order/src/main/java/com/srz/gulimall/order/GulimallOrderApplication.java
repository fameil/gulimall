package com.srz.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author srz
 * @create 2022/9/8 0:42
 * 使用RabbitMQ
 * 1、引入amqp场景; RabbitAutoConfiguration就会 自动生效
 * 2、给容器中自动配置了
 *   RabbitTemplate、AmqpAdmin、 CachingConnectionFactory、 RabbitMessagingTemplate
 *   所有属性都是
 *   @ConfigurationProperties(
 *     prefix = "spring.rabbitmq"
 * )
 *
 * 3、给配置文件中配置
 *      spring.rabbitmq
 * 4、@EnableRabbit: @EnabLeXxxxx 开启功能
 * 5、监听消息：
 *       @RabbitListener：类+方法上
 *       @RabbitHandler: 标在方法上
 */

@EnableFeignClients
@EnableRedisHttpSession
@EnableRabbit
@MapperScan("com.srz.gulimall.order.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class,args);
    }
}
