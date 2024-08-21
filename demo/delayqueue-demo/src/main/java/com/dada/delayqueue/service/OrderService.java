package com.dada.delayqueue.service;


import com.dada.delayqueue.entity.Order;

public interface OrderService {

    /**
     * 将下单信息保存到数据库，并发送至消息队列
     */
    void saveOrder(Order order);

    /**
     * 修改订单状态
     * @param orderId
     */
    void updateOrder(String orderId);

}
