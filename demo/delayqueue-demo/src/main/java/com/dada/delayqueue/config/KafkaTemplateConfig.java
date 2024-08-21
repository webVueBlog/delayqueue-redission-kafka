package com.dada.delayqueue.config;



import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;

/**
 * @Description: kafka配置
 */
@Configuration
public class KafkaTemplateConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAPS_SERVERS;//kafka集群地址

    @Value("${delay.kafka.topic}")
    private String kafkaDelayTopic;//延时队列topic

    @Bean
    public KafkaTemplate kafkaTemplate(){
        //配置
        Properties properties = new Properties();
        //连接集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAPS_SERVERS);
        //指定key,value的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return new KafkaTemplate(new DefaultKafkaProducerFactory(properties));//创建kafkaTemplate
    }
}


