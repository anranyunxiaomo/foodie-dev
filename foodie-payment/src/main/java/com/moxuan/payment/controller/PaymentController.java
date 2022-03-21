package com.moxuan.payment.controller;

import com.moxuan.payment.pojo.bo.MerchantOrdersBO;
import com.moxuan.payment.service.OrdersService;
import com.moxuan.utils.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "payment")
public class PaymentController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 创建商户订单信息
     */
    @PostMapping("/createMerchantOrder")
    public BaseResp createMerchantOrder(@RequestBody @Validated MerchantOrdersBO merchantOrdersBO, HttpServletRequest request, HttpServletResponse response){
     return  ordersService.createMerchantOrder(merchantOrdersBO,request,response);
    }

    /******************************************  以下所有方法开始支付流程   ******************************************/
    /**
     * 拉去微信支付二维码 （native）
     */
    @PostMapping("/getWXPayQRCode")
    public BaseResp getWXPayQRCode(String merchantOrderId, String merchantUserId) throws Exception {
        return ordersService.getWXPayQRCode(merchantOrderId, merchantUserId);
    }

    /**
     * 前往支付宝进行支付
     */
    @ResponseBody
    @RequestMapping(value="/goAlipay")
    public BaseResp goAlipay(String merchantOrderId, String merchantUserId) throws Exception{
        return ordersService.goAlipay(merchantOrderId,merchantUserId);
    }
}
