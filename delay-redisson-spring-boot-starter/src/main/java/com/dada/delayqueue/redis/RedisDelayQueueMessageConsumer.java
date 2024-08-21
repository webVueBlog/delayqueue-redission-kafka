package com.dada.delayqueue.redis;

import com.dada.delayqueue.constants.Constant;
import com.dada.delayqueue.kafka.KafkaMessageProducer;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Classname DelayQueueMessageConsumer
 * @Description 延迟队列消费者,负责将redis延迟队列的信息添加至kafka消息队列
 */
@Slf4j
public class RedisDelayQueueMessageConsumer {

    private DelayQueueMessage delayQueueMessage;// 延迟队列

    private KafkaMessageProducer kafkaSimpleMessageProducer;// 消息生产者

    private String registerService;// 服务注册名称

    public RedisDelayQueueMessageConsumer(DelayQueueMessage delayQueueMessage,
                                          KafkaMessageProducer kafkaSimpleMessageProducer,
                                     String registerService){
        this.delayQueueMessage = delayQueueMessage;
        this.kafkaSimpleMessageProducer = kafkaSimpleMessageProducer;
        this.registerService = registerService;
    }


    /**
     * 应用启动前执行
     * @throws Exception
     */
    @PostConstruct
    public void consumerAndSendKafka() throws Exception {
        //线程池获取redisson延迟队列的值
        /**
         * TODO: 优化：使用线程池
         * ExecutorService executorService = Executors.newFixedThreadPool(10)
         * Executors.newSingleThreadExecutor()
         * submit()
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            String QUEUE_CODE = String.format(Constant.QUEUE_CODE, registerService);
            while (true) {
                //获取redis消息队列里面的值
                Object obj = delayQueueMessage.getDelayQueueMessage(QUEUE_CODE);
                if(obj != null){
                    ///将redis延迟队列的值封装成kafka消息
                    RedisDelayMessage message = RedisDelayMessage.class.cast(obj);
                    message.setTopic(Constant.KAFKA_DELAY_TOPIC);
                    //将从redis获取的值发送到kafka进行消费，kafka接收到消息立即消费
                    kafkaSimpleMessageProducer.sendSimpleMessage(message.getTopic(), message.getValue());
                    log.info("redisson延迟队列发送消息到kafka,主题topic:{}, 消息：{}", message.getTopic(), message.getValue());
                }
                Thread.sleep(500);
            }
        });
        log.info("(Redisson延迟队列启动成功)");
    }
}
