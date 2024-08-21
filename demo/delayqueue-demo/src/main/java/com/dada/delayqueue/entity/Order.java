package com.dada.delayqueue.entity;

import lombok.Data;

@Data
public class Order {

    public Integer orderId;

    public Integer userId;

    /**
     * status支付状态 0未支付 1已支付 2支付已经取消
     */
    public Integer status;
}
