package com.dada.delayqueue.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Classname RedissonDelayQueue
 * @Description Redisson 延迟队列
 */
@Slf4j
public class RedissonDelayQueue implements DelayQueueMessage {

    /**
     * redisson 客户端
     */
    private RedissonClient redissonClient;

    public RedissonDelayQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 添加延迟队列
     * @param value     待添加至队列的值
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueName 队列名称
     * @param <T>
     */
    @Override
    public <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueName) {
        try {
            //获取阻塞队列
            RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueName);
            //根据阻塞队列获取延迟队列
            RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            //将消息添加至redisson的延迟队列，并设置延迟时间
            delayedQueue.offer(value, delay, timeUnit);
            log.info("(添加至延时队列成功) 队列键：{}，队列值：{}，延迟时间：{}", queueName, value, timeUnit.toSeconds(delay) + "秒");
        } catch (Exception e) {
            log.error("(添加至延时队列失败) {}", e.getMessage());
            throw new RuntimeException("(添加延时队列失败)");
        }
    }

    /**
     * 获取延迟队列中的消息
     * @param queueName
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    @Override
    public <T> T getDelayQueueMessage(String queueName) {
        //获取阻塞队列
        RBlockingDeque<Map> blockingDeque = redissonClient.getBlockingDeque(queueName);
        T value;
        try {
            value = (T) blockingDeque.take();//获取并移除队列头部的元素
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

}
