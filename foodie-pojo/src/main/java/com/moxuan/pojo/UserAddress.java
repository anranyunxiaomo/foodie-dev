package com.moxuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_address")
public class UserAddress {
    /**
     * 地址主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 关联用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 收件人姓名
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 收件人手机号
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 区县
     */
    @TableField(value = "district")
    private String district;

    /**
     * 详细地址
     */
    @TableField(value = "detail")
    private String detail;

    /**
     * 扩展字段
     */
    @TableField(value = "extand")
    private String extand;

    /**
     * 是否默认地址 1:是  0:否
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;
}