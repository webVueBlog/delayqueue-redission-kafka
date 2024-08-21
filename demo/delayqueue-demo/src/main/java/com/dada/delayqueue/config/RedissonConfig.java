package com.dada.delayqueue.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Redisson配置
 */
@Configuration
public class RedissonConfig {

    /**
     * 创建RedissonClient对象
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://43.142.255.173:6379").setPassword("xxxxxx");// 设置redis的连接地址和密码
        //useSingleServer()方法表示单机模式，useMasterSlaveServers()方法表示主从模式，useSentinelServers()方法表示哨兵模式
        // 创建RedissonClient对象
        return Redisson.create(config);
    }
}

