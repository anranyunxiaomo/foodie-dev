package com.moxuan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.OrderStatusMapper;
import com.moxuan.pojo.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderStatusService extends ServiceImpl<OrderStatusMapper, OrderStatus> {

    /**
     * 保存订单状态表
     */
    public void add(String orderId) {
        OrderStatus orderStatus = new OrderStatus(orderId, 10, new Date());
        this.save(orderStatus);
    }
}

