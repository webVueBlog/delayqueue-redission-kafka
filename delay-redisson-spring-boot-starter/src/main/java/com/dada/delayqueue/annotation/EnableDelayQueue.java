package com.dada.delayqueue.annotation;

import com.dada.delayqueue.config.DelayQueueAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description: 开启延时队列
 * @Target({ElementType.TYPE}) //注解的作用目标
 * @Retention(RetentionPolicy.RUNTIME) //注解的保留策略
 * @Documented //注解是否将包含在JavaDoc中
 * @Import({DelayQueueAutoConfiguration.class}) // 导入配置类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DelayQueueAutoConfiguration.class})
public @interface EnableDelayQueue {

}
