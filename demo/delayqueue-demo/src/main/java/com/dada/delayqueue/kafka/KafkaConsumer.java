package com.dada.delayqueue.kafka;


import com.dada.delayqueue.constant.Constant;
import com.dada.delayqueue.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Description: kafka消费者
 */
@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private OrderService orderService;

    /**
     * 监听订单创建超时未支付的消息
     * @param orderId
     */
    @KafkaListener(topics = Constant.QUEUE_NOT_PAID)
    public void OrderListener(String orderId){
        log.info("topic: {}，监听到订单创建超时未支付的消息，orderId:{}开始修改数据库信息关闭订单",Constant.QUEUE_NOT_PAID,orderId);
        //TODO 根据订单号查询数据库，判断用户是否支付
        //如果没有支付 修改订单状态，修改支付状态为2,表示订单超时未支付
        orderService.updateOrder(orderId);
    }
}
