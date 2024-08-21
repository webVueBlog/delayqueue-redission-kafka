package com.dada.delayqueue.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Classname RedissonDelayProperties
 */
@Data
@Configuration
//@Component
public class RedissonDelayProperties {

    /**
     * 读取properies文件的值
     */
    @Value("${groot.redis.delay.service: other}")
    private String registerService;//注册服务

}
