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
 * 商品参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "items_param")
public class ItemsParam {
    /**
     * 商品参数id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商品外键id
     */
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 产地 产地，例：中国江苏
     */
    @TableField(value = "produc_place")
    private String producPlace;

    /**
     * 保质期 保质期，例：180天
     */
    @TableField(value = "foot_period")
    private String footPeriod;

    /**
     * 品牌名 品牌名，例：三只大灰狼
     */
    @TableField(value = "brand")
    private String brand;

    /**
     * 生产厂名 生产厂名，例：大灰狼工厂
     */
    @TableField(value = "factory_name")
    private String factoryName;

    /**
     * 生产厂址 生产厂址，例：大灰狼生产基地
     */
    @TableField(value = "factory_address")
    private String factoryAddress;

    /**
     * 包装方式 包装方式，例：袋装
     */
    @TableField(value = "packaging_method")
    private String packagingMethod;

    /**
     * 规格重量 规格重量，例：35g
     */
    @TableField(value = "weight")
    private String weight;

    /**
     * 存储方法 存储方法，例：常温5~25°
     */
    @TableField(value = "storage_method")
    private String storageMethod;

    /**
     * 食用方式 食用方式，例：开袋即食
     */
    @TableField(value = "eat_method")
    private String eatMethod;

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