package com.moxuan.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.OrderStatusMapper;
import com.moxuan.pojo.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderStatusService extends ServiceImpl<OrderStatusMapper, OrderStatus> {

    /**
     * 保存订单状态表
     */
    public void add(String orderId) {
        OrderStatus orderStatus = new OrderStatus(orderId, 10, new Date());
        this.save(orderStatus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {
        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        List<OrderStatus> orderStatusList = this.lambdaQuery().eq(OrderStatus::getOrderStatus, 10).list();
        if (CollUtil.isNotEmpty(orderStatusList)) {
            for (OrderStatus orderStatus : orderStatusList) {
                Date createdTime = orderStatus.getCreatedTime();
                // 拿订单创建时间跟当前时间比较
                long between = DateUtil.between(createdTime, new Date(), DateUnit.DAY);
                if (between >= 1) {
                    // 关闭订单
                    this.doCloseOrder(orderStatus.getOrderId());
                }

            }
        }
    }

    private void doCloseOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(50);
        orderStatus.setCloseTime(new Date());
        this.update(orderStatus, Wrappers.<OrderStatus>lambdaUpdate()
                .eq(OrderStatus::getOrderId, orderId));


    }
}

