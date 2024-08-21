package com.dada.delayqueue.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * kafka消息生产器
 */
@Slf4j
public class KafkaMessageProducer {
    /**
     * kafka消息发送模板
     */
    private KafkaTemplate<Long, String> kafkaTemplate;

    /**
     * 构造方法 KafkaMessageProducer构造方法用于 初始化kafkaTemplate，注解
     * @Autowired会自动注入KafkaTemplate
     * @Qualifier("kafkaTemplate")指定注入的bean名称为kafkaTemplate
     *
     * @param kafkaTemplate
     */
    public KafkaMessageProducer(KafkaTemplate<Long, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送消息
     * topic：主题
     * jsonSerializableObject：待序列化的对象
     * sendSimpleMessage：发送消息
     * @param topic
     * @param jsonSerializableObject
     * @param <T>
     */
    public <T> void sendSimpleMessage(String topic, T jsonSerializableObject) {
        sendSimpleMessage(topic,  jsonSerializableObject,null);
    }


    /**
     * 简单的发送消息的方法
     * @param topic 主题
     * @param jsonSerializableObject 待序列化的对象
     * @param features 序列化
     * @param <T>
     */
    public <T> void sendSimpleMessage(String topic, T jsonSerializableObject, SerializerFeature... features) {
        String body ;
        if(features == null){//features == null 表示不指定序列化
             body = JSON.toJSONString(jsonSerializableObject);
        }else{
             body = JSON.toJSONString(jsonSerializableObject, features);
        }
        //发送消息
        ListenableFuture<SendResult<Long, String>> future = kafkaTemplate.send(topic, body);
        //回调方法
        future.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            //发送失败回调
            @Override
            public void onFailure(@NonNull Throwable throwable) {
                log.error("Sending message to Kafka failed, topic: {}, body: {}", topic, body);
                log.error(throwable.getMessage(), throwable);
            }

            //发送成功回调
            @Override
            public void onSuccess(SendResult<Long, String> result) {
                log.info("Sending message to Kafka finished, topic: {}, body: {}, result: {}", topic, body, JSON.toJSONString(result));
            }
        });
    }
}
