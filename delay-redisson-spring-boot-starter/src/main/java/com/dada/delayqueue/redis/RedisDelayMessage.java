package com.dada.delayqueue.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * redission的延迟队列中的消息 包含主题和消息属性
 * @NoArgsConstructor:无参构造函数
 * @Data:生成get/set方法
 * @AllArgsConstructor:全参构造函数
 * @Classname RedisDelayMssage
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisDelayMessage<T> implements Serializable {

    private String topic;

    private T value;

}
