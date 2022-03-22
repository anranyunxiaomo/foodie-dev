package com.moxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.pojo.Orders;
import com.moxuan.vo.center.MyOrdersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
    Page<MyOrdersVO> queryMyOrders(Page<MyOrdersVO> page, @Param("userId") String userId, @Param("orderStatus") Integer orderStatus);
}