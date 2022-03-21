﻿package com.moxuan.payment.service;

import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.payment.resource.AliPayResource;
import com.moxuan.payment.wx.entity.PayResult;
import com.moxuan.payment.wx.service.WxOrderService;
import com.moxuan.utils.CurrencyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Slf4j
public class NoticeService extends ServiceImpl {

    @Autowired
    private WxOrderService wxOrderService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AliPayResource aliPayResource;

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    /**
     * 微信异步通知支付信息回调接口
     */

    public void wxpay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PayResult wxPayResult = wxOrderService.getWxPayResult(request.getInputStream());
        boolean isPaid = wxPayResult.getReturn_code().equals("SUCCESS");
        PrintWriter writer = response.getWriter();
        if (isPaid) {
            // 获取到商户支付成功所需的推送的url
            // ====================== 操作商户自己的业务，比如修改订单状态等 start ==========================
            String url = ordersService.updateOrderPaid(wxPayResult.getOut_trade_no(), wxPayResult.getTotal_fee());
            // ============================================ 业务结束， end ==================================
            log.info("************* 支付成功(微信支付异步通知) - 时间: {} *************", new Date());
            log.info("* 商户订单号: {}", wxPayResult.getMch_id());
            log.info("* 微信订单号: {}", wxPayResult.getTransaction_id());
            log.info("* 实际支付金额: {}", wxPayResult.getTotal_fee());
            log.info("*****************************************************************************");
            // 通知天天吃货服务端订单已支付
            notifyFoodie(wxPayResult.getMch_id(), url);
            // 通知微信已经收到消息，不要再给我发消息了，否则微信会10连击调用本接口
            String noticeStr = setXML("SUCCESS", "");
            writer.write(noticeStr);
            writer.flush();
        } else {
            System.out.println("================================= 支付失败 =================================");
            // 支付失败
            String noticeStr = setXML("FAIL", "");
            writer.write(noticeStr);
            writer.flush();
        }

    }

    private void notifyFoodie(String merchantOrderId, String url) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("merchantOrderId", merchantOrderId);
        String httpStatus = HttpUtil.post(url, resultMap);
        log.info("*** 通知天天吃货后返回的状态码 httpStatus: {} ***", httpStatus);
    }

    /**
     * @Description: 支付成功后的支付宝异步通知
     */
    public String alipay(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, UnsupportedEncodingException {
        log.info("支付成功后的支付宝异步通知");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                aliPayResource.getAlipayPublicKey(),
                aliPayResource.getCharset(),
                aliPayResource.getSignType()); //调用SDK验证签名

        if (signVerified) {//验证成功
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            if (trade_status.equals("TRADE_SUCCESS")) {
                String merchantReturnUrl = this.ordersService.updateOrderPaid(out_trade_no, CurrencyUtils.getYuan2Fen(total_amount));
                // 如果这个步骤出现问题了  手动补单
                notifyFoodie(out_trade_no, merchantReturnUrl);
            }
            log.info("************* 支付成功(支付宝异步通知) - 时间: {} *************", new Date());
            log.info("* 订单号: {}", out_trade_no);
            log.info("* 支付宝交易号: {}", trade_no);
            log.info("* 实付金额: {}", total_amount);
            log.info("* 交易状态: {}", trade_status);
            log.info("*****************************************************************************");
            return "success";
        } else {
            //验证失败
            log.info("验签失败, 时间: {}", new Date());
            return "fail";
        }
    }

}