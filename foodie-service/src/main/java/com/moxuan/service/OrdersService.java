package com.moxuan.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.pojo.Orders;
import com.moxuan.mapper.OrdersMapper;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {

}


