package com.dada.delayqueue.redis;

import com.alibaba.fastjson.JSON;
import com.dada.delayqueue.constants.Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Classname DelayQueueMessageProducer
 * @Description redission 延迟队列生产者
 */
@Slf4j
public class RedisDelayQueueMessageProducer {

    /**
     * 消息
     */
    private DelayQueueMessage delayQueueMessage;

    private String registerService;// 服务注册名称

    public RedisDelayQueueMessageProducer(DelayQueueMessage delayQueueMessage, String registerService){
        this.delayQueueMessage = delayQueueMessage;
        this.registerService = registerService;
    }

    /**
     * 发送消息
     * @param topic 主题
     * @param jsonSerializableObject 序列化后的对象
     * @param delay 延迟时间
     * @param timeUnit 时间单位
     * @param <T>
     */
    public <T> void sendMessage(String topic, T jsonSerializableObject, long delay, TimeUnit timeUnit) {
        RedisDelayMessage message = new RedisDelayMessage(topic, jsonSerializableObject);
        delayQueueMessage.addDelayQueue(message, delay, timeUnit, String.format(Constant.QUEUE_CODE, registerService));// 添加到延迟队列
        log.info("添加消息：{} 进入redisson延迟队列，topic ：{} ，delay:{} ,timeUnit:{}", JSON.toJSONString(message), topic, delay, timeUnit);
    }
}
