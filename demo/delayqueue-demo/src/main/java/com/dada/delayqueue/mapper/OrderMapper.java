package com.dada.delayqueue.mapper;


import com.dada.delayqueue.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description:Repository
 * @Repository: 标记存储层组件
 * @Mapper: 标记Mapper接口
 */
@Repository
@Mapper
public interface OrderMapper {

    void save(Order order);


    void update(@Param("orderId")String orderId);
}
