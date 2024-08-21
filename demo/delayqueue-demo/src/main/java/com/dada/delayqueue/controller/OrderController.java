package com.dada.delayqueue.controller;

import com.dada.delayqueue.entity.Order;
import com.dada.delayqueue.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    /**
     * 注入订单服务
     */
    @Autowired
    private OrderService orderService;

    /**
     * 添加订单
     * @param order
     */
    @PostMapping("/order")
    public void addOrder(@RequestBody Order order){
        orderService.saveOrder(order);
    }
}
