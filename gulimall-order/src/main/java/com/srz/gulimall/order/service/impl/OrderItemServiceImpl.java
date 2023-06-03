package com.srz.gulimall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.srz.gulimall.order.entity.OrderEntity;
import com.srz.gulimall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.srz.common.utils.PageUtils;
import com.srz.common.utils.Query;

import com.srz.gulimall.order.dao.OrderItemDao;
import com.srz.gulimall.order.entity.OrderItemEntity;
import com.srz.gulimall.order.service.OrderItemService;

@RabbitListener(queues = {"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    //声明需要监听的所有队列
    //org.springframework.amqp.core.Message
    //1、Message message:原生消息详细信息。头+体
    //2、T<发送的消息类型> OrderReturnReasonEntity content
    //Channel channel :当前传输数据的通道
    //Queue: 可以很多人来监听，只要收到消息，队列消息，而且只能有一个收到消息
    //   订单服务启动多个：同一个消息，只能有一个客户端收到
    //   只有一个消息完全处理完，方法运行结束才接收下一个消息
    //@RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void recievedMessage(Message message, OrderReturnReasonEntity content,
                                Channel channel) throws InterruptedException {
        //消息体
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties properties = message.getMessageProperties();
        System.out.println("recievedMessage接收到消息..内容："+content+"消息类型："+message.getClass());
        //channel内按顺序自增的
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //签收货物，非批量模式
        try {
            if (deliveryTag%2 == 0 ){
                //收货
                channel.basicAck(deliveryTag,false);
                System.out.println("签收了"+deliveryTag+"号货物");
            } else {
                //消息号，是否批量，是否回收消息
                channel.basicNack(deliveryTag,false,true);
                System.out.println("拒收了"+deliveryTag+"号货物");
            }

        } catch (Exception e){
            //网络中断
        }

    }

    @RabbitHandler
    public void recievedMessage2(OrderEntity content) throws InterruptedException {
        //消息体
        System.out.println("recievedMessage2接收到消息..内容："+content);

    }

}