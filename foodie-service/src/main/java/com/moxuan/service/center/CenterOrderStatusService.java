package com.moxuan.service.center;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.OrderStatusMapper;
import com.moxuan.pojo.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterOrderStatusService extends ServiceImpl<OrderStatusMapper, OrderStatus> {

    /**
     * 模拟商家发货
     */
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(30);
        orderStatus.setDeliverTime(new Date());
        this.updateById(orderStatus);

    }

    /**
     * 用户确认收货
     */
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(40);
        orderStatus.setSuccessTime(new Date());
        return this.update(orderStatus, Wrappers.<OrderStatus>lambdaUpdate()
                .eq(OrderStatus::getOrderId, orderId)
                .eq(OrderStatus::getOrderStatus, 30));
    }

    /**
     * 修改订单状态表的留言时间
     */
    public void updateOrdersStatusCommentTime(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        this.updateById(orderStatus);
    }
}
