package com.dada.delayqueue.redis;

import java.util.concurrent.TimeUnit;

/**
 * 延迟队列的接口
 */
public interface DelayQueueMessage {

    /**
     * 添加延迟队列消息
     * @param value
     * @param delay
     * @param timeUnit
     * @param queueName
     * @param <T>
     */
    <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueName);

    /**
     * 获取延迟队列消息
     * @param queueName
     * @return
     * @param <T>
     */
    <T> T getDelayQueueMessage(String queueName);

}
