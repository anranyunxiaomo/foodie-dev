package com.moxuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 订单表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "orders")
public class Orders {
    /**
     * 订单主键 同时也是订单编号
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 收货人快照
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    /**
     * 收货人手机号快照
     */
    @TableField(value = "receiver_mobile")
    private String receiverMobile;

    /**
     * 收货地址快照
     */
    @TableField(value = "receiver_address")
    private String receiverAddress;

    /**
     * 订单总价格
     */
    @TableField(value = "total_amount")
    private Integer totalAmount;

    /**
     * 实际支付总价格
     */
    @TableField(value = "real_pay_amount")
    private Integer realPayAmount;

    /**
     * 邮费 默认可以为零，代表包邮
     */
    @TableField(value = "post_amount")
    private Integer postAmount;

    /**
     * 支付方式 1:微信 2:支付宝
     */
    @TableField(value = "pay_method")
    private Integer payMethod;

    /**
     * 买家留言
     */
    @TableField(value = "left_msg")
    private String leftMsg;

    /**
     * 扩展字段
     */
    @TableField(value = "extand")
    private String extand;

    /**
     * 买家是否评价 1：已评价，0：未评价
     */
    @TableField(value = "is_comment")
    private Integer isComment;

    /**
     * 逻辑删除状态 1: 删除 0:未删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间（成交时间）
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;
}