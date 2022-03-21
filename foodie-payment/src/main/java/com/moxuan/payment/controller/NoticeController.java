package com.moxuan.payment.controller;

import com.moxuan.payment.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/payment/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value="/wxpay")
    public void wxpay(HttpServletRequest request, HttpServletResponse response) throws Exception {
       // 支付成功后的微信支付异步通知
        noticeService.wxpay(request,response);
    }

    /**
     * @Description: 支付成功后的支付宝异步通知
     */
    @RequestMapping(value="/alipay")
    public String alipay(HttpServletRequest request, HttpServletResponse response) throws Exception {
     return  noticeService.alipay(request,response);
    }

}
