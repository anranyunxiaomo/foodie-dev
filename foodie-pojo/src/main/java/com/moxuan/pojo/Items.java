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
 * 商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "items")
public class Items {
    /**
     * 商品主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商品名称 商品名称
     */
    @TableField(value = "item_name")
    private String itemName;

    /**
     * 分类外键id 分类id
     */
    @TableField(value = "cat_id")
    private Integer catId;

    /**
     * 一级分类外键id 一级分类id，用于优化查询
     */
    @TableField(value = "root_cat_id")
    private Integer rootCatId;

    /**
     * 累计销售 累计销售
     */
    @TableField(value = "sell_counts")
    private Integer sellCounts;

    /**
     * 上下架状态 上下架状态,1:上架 2:下架
     */
    @TableField(value = "on_off_status")
    private Integer onOffStatus;

    /**
     * 商品内容 商品内容
     */
    @TableField(value = "content")
    private String content;

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