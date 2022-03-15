package com.moxuan.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.pojo.OrderStatus;
import com.moxuan.mapper.OrderStatusMapper;

@Service
public class OrderStatusService extends ServiceImpl<OrderStatusMapper, OrderStatus> {

}

