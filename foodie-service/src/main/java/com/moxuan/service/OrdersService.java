package com.moxuan.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.ao.MountAO;
import com.moxuan.bo.SubmitOrderBO;
import com.moxuan.payment.config.PaymentProperties;
import com.moxuan.mapper.OrdersMapper;
import com.moxuan.pojo.OrderStatus;
import com.moxuan.pojo.Orders;
import com.moxuan.pojo.UserAddress;
import com.moxuan.pojo.mapper.MerchantOrdersVOBeanMapper;
import com.moxuan.pojo.mapper.OrdersBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.CustomException;
import com.moxuan.utils.ResultUtil;
import com.moxuan.vo.MerchantOrdersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private Sid sid;
    @Resource
    private OrdersBeanMapper ordersBeanMapper;
    @Autowired
    private OrderStatusService orderStatusService;
    @Resource
    private MerchantOrdersVOBeanMapper merchantOrdersVOBeanMapper;
    @Autowired
    private PaymentProperties paymentProperties;


    /**
     * 用户下单 （创建订单）
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BaseResp create(SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        // 1. 创建订单 (1) 根据提供的参数 填充 订单表中的数据信息 table - Orders
        UserAddress userAddress = userAddressService.lambdaQuery()
                .eq(UserAddress::getUserId, submitOrderBO.getUserId())
                .eq(UserAddress::getId, submitOrderBO.getAddressId()).one();
        if (ObjectUtil.isNull(userAddress)) {
            throw new CustomException(ResultUtil.error("未找到该用户对应的地址"));
        }
        //      根据提供的参数 查询订单规格 计算 填充 订单商品关联表 table - OrderItems
        //      2. 循环根据itemSpecIds保存订单商品信息表 该步骤中需要处理订单超买 超卖的问题。需要更新商品规格表中 商品的数量
        // 商户订单ID
        String orderId = sid.nextShort();
        MountAO mountAO = orderItemsService.add(orderId, submitOrderBO.getItemSpecIds());
        // 包邮费用设置为0
        Integer postAmount = 0;
        Orders orders = ordersBeanMapper.combination(orderId, submitOrderBO, userAddress, postAmount, mountAO);
        this.save(orders);
        // 3. 保存订单状态表
        orderStatusService.add(orderId);
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = merchantOrdersVOBeanMapper.combination(orderId,
                submitOrderBO.getUserId(),
                mountAO.getRealPayAmount() + postAmount,
                submitOrderBO.getPayMethod(),
                paymentProperties.getCallback());
        String post = HttpUtil.post(paymentProperties.getNotice(), JSONUtil.toJsonStr(merchantOrdersVO));
        BaseResp baseResp = JSONUtil.toBean(post, BaseResp.class);
        if (baseResp.getStatus() != 200) {
            throw new CustomException(ResultUtil.error("支付中心创建订单失败"));
        }
        return ResultUtil.ok(orderId);
    }

    /**
     * 支付中心通知商家订单已支付
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer notifyMerchantOrderPaid(String merchantOrderId, int i) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(i);
        orderStatus.setPayTime(new Date());
        this.orderStatusService.update(orderStatus, Wrappers.<OrderStatus>lambdaUpdate().eq(OrderStatus::getOrderId,merchantOrderId));
        return  HttpStatus.OK.value();
    }
}


