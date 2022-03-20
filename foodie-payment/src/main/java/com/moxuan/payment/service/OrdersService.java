package com.moxuan.payment.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.payment.mapper.OrdersMapper;
import com.moxuan.payment.pojo.Orders;
import org.springframework.stereotype.Service;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {

}
