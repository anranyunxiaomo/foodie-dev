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
 * 用户表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users")
public class Users {
    /**
     * 主键id 用户id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 用户名 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 昵称 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 真实姓名 真实姓名
     */
    @TableField(value = "realname")
    private String realname;

    /**
     * 头像 头像
     */
    @TableField(value = "face")
    private String face;

    /**
     * 手机号 手机号
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 邮箱地址 邮箱地址
     */
    @TableField(value = "email")
    private String email;

    /**
     * 性别 性别 1:男  0:女  2:保密
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 生日 生日
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 创建时间 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 更新时间 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;
}