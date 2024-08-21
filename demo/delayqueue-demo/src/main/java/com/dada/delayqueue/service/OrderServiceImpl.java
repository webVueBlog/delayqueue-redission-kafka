package com.dada.delayqueue.service;


import com.dada.delayqueue.constants.Constant;
import com.dada.delayqueue.redis.RedisDelayQueueMessageProducer;
import com.dada.delayqueue.entity.Order;
import com.dada.delayqueue.mapper.OrderMapper;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService{

    /**
     * redisson客户端
     */
    @Autowired
    private RedissonClient redissonClient;

    /**
     * redisson延时队列消息生产者
     */
    @Autowired
    private RedisDelayQueueMessageProducer redisDelayQueueMessageProducer;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void saveOrder(Order order) {

        //添加至数据库
        orderMapper.save(order);
        //发送延时信息至redisson
        /**
         * 发送延时消息
         * 第一个参数：队列名称
         * 第二个参数：消息内容
         * 第三个参数：延时时间
         * 第四个参数：延时时间单位
         * 例如：8秒后执行
         */
        redisDelayQueueMessageProducer.sendMessage(Constant.QUEUE_CODE,order.getOrderId(),8,TimeUnit.SECONDS);

    }

    @Override
    public void updateOrder(String orderId) {
        orderMapper.update(orderId);
    }
}
