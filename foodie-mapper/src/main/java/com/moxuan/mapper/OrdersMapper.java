package com.moxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.ao.center.MyOrderStatusCountsAO;
import com.moxuan.pojo.OrderStatus;
import com.moxuan.pojo.Orders;
import com.moxuan.vo.center.MyOrdersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
    Page<MyOrdersVO> queryMyOrders(Page<MyOrdersVO> page, @Param("userId") String userId, @Param("orderStatus") Integer orderStatus);

    /**
     * 获得订单状态数概况
     */
    List<MyOrderStatusCountsAO> getMyOrderStatusCounts(@Param("userId") String userId);

    Page<OrderStatus> getMyOrderTrend(Page<OrderStatus> page, @Param("userId") String userId);
}