package com.dada.delayqueue.config;

import com.dada.delayqueue.kafka.KafkaMessageProducer;
import com.dada.delayqueue.redis.DelayQueueMessage;
import com.dada.delayqueue.redis.RedisDelayQueueMessageConsumer;
import com.dada.delayqueue.redis.RedisDelayQueueMessageProducer;
import com.dada.delayqueue.redis.RedissonDelayProperties;
import com.dada.delayqueue.redis.RedissonDelayQueue;
import lombok.Value;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Classname DelayQueueAutoConfig
 */
@Configuration
//配置文件需要打开该配置
@ConditionalOnProperty(name = "delay.redisson.enable", havingValue = "true")//havingValue = "true"表示默认开启
public class DelayQueueAutoConfiguration {

    /**
     * 操作redis string类型的bean
     * @ConditionalOnBean({ RedisConnectionFactory.class })表示当存在RedisConnectionFactory.class时才创建
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)//如果存在StringRedisTemplate则不创建
    @ConditionalOnBean({ RedisConnectionFactory.class })
    public StringRedisTemplate stringRedisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();//创建
        template.setConnectionFactory(redisConnectionFactory);//设置连接工厂
        return template;//返回
    }

    /**
     * redis redission连接工厂
     * @param redisson
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    @ConditionalOnBean({ RedissonClient.class })
    public RedissonConnectionFactory redissonConnectionFactory(@Autowired RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);//返回
    }

    /**
     * redis的延迟消息队列
     * RedissonClient是redission的客户端，通过它来操作redis
     * @param redisson
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DelayQueueMessage.class)
    @ConditionalOnBean({ RedissonClient.class })
    public DelayQueueMessage delayQueueMessage(@Autowired RedissonClient redisson){
        return new RedissonDelayQueue(redisson);//返回
    }

    /**
     * redission 延迟消息队列的生产者
     * RedissonDelayProperties是配置文件中关于redisson的配置
     * @ConditionalOnMissingBean(RedisDelayQueueMessageProducer.class)表示如果存在RedisDelayQueueMessageProducer.class则不创建
     * @ConditionalOnBean({ DelayQueueMessage.class, RedissonDelayProperties.class })表示当存在DelayQueueMessage.class和RedissonDelayProperties.class时才创建
     * @Autowired DelayQueueMessage delayQueueMessage表示注入DelayQueueMessage
     * @Autowired RedissonDelayProperties properties表示注入RedissonDelayProperties
     *
     * @param delayQueueMessage
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisDelayQueueMessageProducer.class)
    @ConditionalOnBean({ DelayQueueMessage.class, RedissonDelayProperties.class })
    public RedisDelayQueueMessageProducer delayQueueMessageProducer(@Autowired DelayQueueMessage delayQueueMessage,
                                                               @Autowired RedissonDelayProperties properties){
        return new RedisDelayQueueMessageProducer(delayQueueMessage, properties.getRegisterService());
    }

    /**
     * Kafka消息队列的生产者
     * @Bean表示创建一个bean
     * @ConditionalOnMissingBean(KafkaMessageProducer.class)表示如果存在KafkaMessageProducer.class则不创建
     * @ConditionalOnBean({ KafkaTemplate.class })表示当存在KafkaTemplate.class时才创建
     * @Autowired KafkaTemplate<Long, String> kafkaTemplate表示注入KafkaTemplate<Long, String>
     *     Long是消息的key，String是消息的value
     *     @Autowired RedissonDelayProperties properties表示注入RedissonDelayProperties
     *     @Autowired DelayQueueMessage delayQueueMessage表示注入DelayQueueMessage
     *     @Autowired KafkaTemplate<Long, String> kafkaTemplate表示注入KafkaTemplate<Long, String>
     * @param kafkaTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(KafkaMessageProducer.class)
    @ConditionalOnBean({ KafkaTemplate.class })
    public KafkaMessageProducer kafkaSimpleMessageProducer(@Autowired KafkaTemplate<Long, String> kafkaTemplate){
        return new KafkaMessageProducer(kafkaTemplate);
    }

    /**
     * Kafka消息队列的消费者
     * RedissonDelayProperties是配置文件中关于redisson的配置
     * @ConditionalOnMissingBean(RedisDelayQueueMessageConsumer.class)表示如果存在RedisDelayQueueMessageConsumer.class则不创建
     * @ConditionalOnBean({DelayQueueMessage.class, KafkaMessageProducer.class})表示当存在DelayQueueMessage.class和KafkaMessageProducer.class时才创建
     * @Autowired DelayQueueMessage delayQueueMessage表示注入DelayQueueMessage
     * @Autowired KafkaMessageProducer kafkaSimpleMessageProducer表示注入KafkaMessageProducer
     * @Autowired RedissonDelayProperties properties表示注入RedissonDelayProperties
     * @param delayQueueMessage
     * @param kafkaSimpleMessageProducer
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RedisDelayQueueMessageConsumer.class)
    @ConditionalOnBean({DelayQueueMessage.class, KafkaMessageProducer.class})
    public RedisDelayQueueMessageConsumer delayQueueMessageConsumer(@Autowired DelayQueueMessage delayQueueMessage,
                                                               @Autowired KafkaMessageProducer kafkaSimpleMessageProducer,
                                                               @Autowired RedissonDelayProperties properties) {
        return new RedisDelayQueueMessageConsumer(delayQueueMessage, kafkaSimpleMessageProducer, properties.getRegisterService());
    }

}
