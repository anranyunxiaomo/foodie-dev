package com.moxuan.payment.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 订单表;
 */
@Data
@TableName(value = "orders")
public class Orders {
    /**
     * 订单主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商户订单号
     */
    @TableField(value = "merchant_order_id")
    private String merchantOrderId;

    /**
     * 商户方的发起用户的用户主键id
     */
    @TableField(value = "merchant_user_id")
    private String merchantUserId;

    /**
     * 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    @TableField(value = "amount")
    private Integer amount;

    /**
     * 支付方式
     */
    @TableField(value = "pay_method")
    private Integer payMethod;

    /**
     * 支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     */
    @TableField(value = "pay_status")
    private Integer payStatus;

    /**
     * 从哪一端来的，比如从天天吃货这门实战过来的
     */
    @TableField(value = "come_from")
    private String comeFrom;

    /**
     * 支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     */
    @TableField(value = "return_url")
    private String returnUrl;

    /**
     * 逻辑删除状态;1: 删除 0:未删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间（成交时间）
     */
    @TableField(value = "created_time")
    private Date createdTime;
}