package com.moxuan.payment.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.payment.config.JedisUtil;
import com.moxuan.payment.mapper.OrdersMapper;
import com.moxuan.payment.pojo.Orders;
import com.moxuan.payment.pojo.bo.MerchantOrdersBO;
import com.moxuan.payment.pojo.mapper.OrdersBeanMapper;
import com.moxuan.payment.pojo.mapper.PaymentInfoBeanMapper;
import com.moxuan.payment.pojo.vo.PaymentInfoVO;
import com.moxuan.payment.resource.AliPayResource;
import com.moxuan.payment.resource.WXPayResource;
import com.moxuan.payment.wx.entity.PreOrderResult;
import com.moxuan.payment.wx.service.WxOrderService;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.CurrencyUtils;
import com.moxuan.utils.CustomException;
import com.moxuan.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {

    @Autowired
    private Sid sid;
    @Resource
    private OrdersBeanMapper ordersBeanMapper;
    @Resource
    private JedisUtil jedisUtil;
    @Resource
    private WXPayResource wxPayResource;
    @Autowired
    private WxOrderService wxOrderService;
    @Resource
    private PaymentInfoBeanMapper paymentInfoBeanMapper;
    @Autowired
    private AliPayResource aliPayResource;

    @Autowired
//    private AliPayResource aliPayResource;


    /**
     * 创建商户订单信息
     */
    public BaseResp createMerchantOrder(MerchantOrdersBO merchantOrdersBO, HttpServletRequest request, HttpServletResponse response) {
        String ordersId = sid.nextShort();
        Orders orders = ordersBeanMapper.combination(merchantOrdersBO, ordersId);
        boolean save = this.save(orders);
        if (save) {
            return ResultUtil.ok("商品订单创建成功");
        }
        throw new CustomException(ResultUtil.error("商户订单创建失败"));
    }

    /**
     * 拉去微信支付二维码 （native）
     * merchantOrderId : 商户唯一标识
     * merchantUserId : 用户唯一标识
     */
    public BaseResp getWXPayQRCode(String merchantOrderId, String merchantUserId) throws Exception {
        if (StrUtil.isBlankIfStr(merchantOrderId) || StrUtil.isBlankIfStr(merchantUserId)) {
            return ResultUtil.error("查询参数不能为空");
        }
        Orders orders = this.lambdaQuery().eq(Orders::getMerchantOrderId, merchantOrderId)
                .eq(Orders::getMerchantUserId, merchantUserId)
                .eq(Orders::getPayStatus, 10).one();
        // 商品描述
        String body = "天天吃货-付款用户[" + merchantUserId + "]";
        // 商户订单号
        String out_trade_no = merchantOrderId;
        // 从redis中去获得这笔订单的微信支付二维码，如果订单状态没有支付没有就放入，这样的做法防止用户频繁刷新而调用微信接口
        if (orders != null) {
            String qrCodeUrl = jedisUtil.get(wxPayResource.getQrcodeKey() + ":" + merchantOrderId);
            if (StrUtil.isBlankIfStr(qrCodeUrl)) {
                // 订单总金额，单位为分
                String total_fee = String.valueOf(orders.getAmount());
//				String total_fee = "1";
                // 统一下单
                PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee);
                qrCodeUrl = preOrderResult.getCode_url();
            }
            PaymentInfoVO paymentInfoVO = paymentInfoBeanMapper.combination(orders.getAmount(), merchantOrderId, merchantUserId, qrCodeUrl);
            // , wxPayResource.getQrcodeExpire() 需要配置超时间
            jedisUtil.set(wxPayResource.getQrcodeKey() + ":" + merchantOrderId, qrCodeUrl);
            return ResultUtil.ok(paymentInfoVO);
        }
        return ResultUtil.error("该订单不存在，或已经支付");
    }


    public String updateOrderPaid(String merchantOrderId, int paidAmount) {
        // 修改该订单的信息状态
        Orders orders = new Orders();
        orders.setPayStatus(20);
        orders.setAmount(paidAmount);
        int update = this.baseMapper.update(orders, Wrappers.<Orders>lambdaUpdate().eq(Orders::getMerchantOrderId, merchantOrderId));
        if (update != 1) {
            throw new CustomException(ResultUtil.error("订单修改异常"));
        }
        orders = this.lambdaQuery().eq(Orders::getMerchantOrderId, merchantOrderId).one();
        return orders.getReturnUrl();
    }

    /**
     * 前往支付宝进行支付
     */
    public BaseResp goAlipay(String merchantOrderId, String merchantUserId) {
        Orders orders = this.lambdaQuery().eq(Orders::getMerchantOrderId, merchantOrderId)
                .eq(Orders::getMerchantUserId, merchantUserId)
                .eq(Orders::getPayStatus, 10)
                .one();
        if (ObjectUtil.isEmpty(orders)) {
            throw new CustomException(ResultUtil.error("未找到相应的商品"));
        }
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayResource.getGatewayUrl(),
                aliPayResource.getAppId(),
                aliPayResource.getMerchantPrivateKey(),
                "json",
                aliPayResource.getCharset(),
                aliPayResource.getAlipayPublicKey(),
                aliPayResource.getSignType());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(aliPayResource.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayResource.getNotifyUrl());
        // 商户订单号, 商户网站订单系统中唯一订单号, 必填
        String out_trade_no = merchantOrderId;
        // 付款金额, 必填 单位元
        String total_amount = CurrencyUtils.getFen2YuanWithPoint(orders.getAmount());
//    	String total_amount = "0.01";	// 测试用 1分钱
        // 订单名称, 必填
        String subject = "天天吃货-付款用户[" + merchantUserId + "]";
        // 商品描述, 可空, 目前先用订单名称
        String body = subject;
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "1d";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\"" + timeout_express + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //若想给BizContent增加其他可选请求参数, 以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
        //请求
        String alipayForm = "";
        try {
            alipayForm = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("支付宝支付 - 前往支付页面, alipayForm: \n{}", alipayForm);
        return ResultUtil.ok(alipayForm);
    }
}
