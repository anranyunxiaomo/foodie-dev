package com.moxuan.service.center;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.ao.center.MyOrderStatusCountsAO;
import com.moxuan.mapper.OrdersMapper;
import com.moxuan.page.PageInfo;
import com.moxuan.pojo.OrderStatus;
import com.moxuan.pojo.Orders;
import com.moxuan.pojo.mapper.center.CenterOrdersBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.center.MyOrdersVO;
import com.moxuan.vo.center.OrderStatusCountsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MyOrdersService extends ServiceImpl<OrdersMapper, Orders> {

    @Resource
    private CenterOrdersBeanMapper centerOrdersBeanMapper;
    @Autowired
    private CenterOrderStatusService CenterOrderStatusService;

    /**
     * 查询订单列表
     */
    public BaseResp queryMyOrders(String userId, Integer orderStatus, PageInfo pageInfo) {
        Page<MyOrdersVO> myOrdersVOPage = new Page<>(pageInfo.getPage(), pageInfo.getPageSize());
        myOrdersVOPage = this.baseMapper.queryMyOrders(myOrdersVOPage, userId, orderStatus);
        PagedGridResult<MyOrdersVO> myOrdersVOPagedGridResult = centerOrdersBeanMapper.toMyOrdersVO(myOrdersVOPage);
        return ResultUtil.ok(myOrdersVOPagedGridResult);
    }

    /**
     * 商家发货没有后端，所以这个接口仅仅只是用于模拟
     */
    public BaseResp deliver(String orderId) {
        if (StrUtil.isBlankIfStr(orderId)) {
            return ResultUtil.error("订单ID不能为空");
        }
        CenterOrderStatusService.updateDeliverOrderStatus(orderId);
        return ResultUtil.ok();
    }

    /**
     * 用户确认收货
     */
    public BaseResp confirmReceive(String orderId, String userId) {
        if (checkUserOrder(orderId, userId)) {
            return ResultUtil.error("该订单属于无效订单");
        }

        boolean res = CenterOrderStatusService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return ResultUtil.error("订单确认收货失败！");
        }

        return ResultUtil.ok();
    }

    /**
     * 用户删除订单
     */
    public BaseResp delete(String orderId, String userId) {
        if (checkUserOrder(orderId, userId)) {
            return ResultUtil.error("该订单属于无效订单");
        }
        Orders orders = new Orders();
        orders.setIsDelete(1);
        orders.setUpdatedTime(new Date());
        boolean res = this.update(orders, Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, orderId)
                .eq(Orders::getUserId, userId));
        if (!res) {
            return ResultUtil.error("订单删除失败！");
        }

        return ResultUtil.ok();

    }

    public boolean checkUserOrder(String orderId, String userId) {
        Orders orders = this.lambdaQuery().eq(Orders::getId, orderId)
                .eq(Orders::getUserId, userId)
                .eq(Orders::getIsDelete, 0)
                .one();
        if (ObjectUtil.isNull(orders)) {
            return true;
        }
        return false;
    }

    // 2. 修改订单表改已评价 orders
    public void updateOrdersComments(String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(1);
        this.updateById(orders);
    }

    /**
     * 获得订单状态数概况
     */
    public BaseResp statusCounts(String userId) {
        List<MyOrderStatusCountsAO> myOrderStatusCounts = this.baseMapper.getMyOrderStatusCounts(userId);
        Integer waitPayCounts = getCount(myOrderStatusCounts, 10);
        Integer waitDeliverCounts = getCount(myOrderStatusCounts, 20);
        Integer waitReceiveCounts = getCount(myOrderStatusCounts, 30);
        Integer waitCommentCounts = getCount(myOrderStatusCounts, 40);
        OrderStatusCountsVO combination = centerOrdersBeanMapper.combination(waitPayCounts, waitDeliverCounts, waitReceiveCounts, waitCommentCounts);
        return ResultUtil.ok(combination);
    }

    /**
     * 查询订单动向
     */
    public BaseResp trend(String userId, PageInfo pageInfo) {
        Page<OrderStatus> page = new Page<>(pageInfo.getPage(), pageInfo.getPageSize());
        page = this.baseMapper.getMyOrderTrend(page, userId);
        PagedGridResult<OrderStatus> orderStatusPagedGridResult = centerOrdersBeanMapper.toOrderStatus(page);
        return ResultUtil.ok(orderStatusPagedGridResult);
    }

    public Integer getCount(List<MyOrderStatusCountsAO> myOrderStatusCounts, Integer orderStatus) {
        return myOrderStatusCounts.stream()
                .filter(MyOrderStatusCountsAO -> MyOrderStatusCountsAO.getOrderStatus() == orderStatus)
                .mapToInt(MyOrderStatusCountsAO::getCount).sum();
    }
}
