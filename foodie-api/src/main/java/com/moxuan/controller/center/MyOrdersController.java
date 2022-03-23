package com.moxuan.controller.center;

import com.moxuan.page.PageInfo;
import com.moxuan.service.center.MyOrdersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("myorders")
public class MyOrdersController {

    @Autowired
    private MyOrdersService myOrdersService;


    /**
     * 查询订单列表
     */
    @PostMapping("query")
    public BaseResp query(@RequestParam String userId,
                          @RequestParam Integer orderStatus,
                          PageInfo pageInfo) {
        return myOrdersService.queryMyOrders(userId, orderStatus, pageInfo);

    }

    /**
     * 商家发货没有后端，所以这个接口仅仅只是用于模拟
     */
    @GetMapping("/deliver")
    public BaseResp deliver(
            @RequestParam String orderId) throws Exception {
        return myOrdersService.deliver(orderId);
    }

    /**
     * 用户确认收货
     */
    @PostMapping("/confirmReceive")
    public BaseResp confirmReceive(
            @RequestParam String orderId,
            @RequestParam String userId) throws Exception {
        return myOrdersService.confirmReceive(orderId, userId);
    }

    /**
     * 用户删除订单
     */
    @PostMapping("/delete")
    public BaseResp delete(
            @RequestParam String orderId,
            @RequestParam String userId) {
        return myOrdersService.delete(orderId, userId);
    }

    /**
     * 获得订单状态数概况
     */
    @PostMapping("/statusCounts")
    public BaseResp statusCounts(@RequestParam String userId) {
        return myOrdersService.statusCounts(userId);
    }

    /**
     * 查询订单动向
     */
    @PostMapping("/trend")
    public BaseResp trend(@RequestParam String userId,
                          PageInfo pageInfo) {
        return myOrdersService.trend(userId, pageInfo);
    }

}
