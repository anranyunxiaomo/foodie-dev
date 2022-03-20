package com.moxuan.controller;

import com.moxuan.bo.SubmitOrderBO;
import com.moxuan.service.OrdersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单 （创建订单）
     */
    @PostMapping("/create")
    public BaseResp create(@RequestBody @Validated SubmitOrderBO submitOrderBO,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        return ordersService.create(submitOrderBO, request, response);
    }


}
