package com.moxuan.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.pojo.OrderItems;
import com.moxuan.mapper.OrderItemsMapper;

@Service
public class OrderItemsService extends ServiceImpl<OrderItemsMapper, OrderItems> {

}

