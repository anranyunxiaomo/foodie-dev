package com.moxuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品规格 每一件商品都有不同的规格，不同的规格又有不同的价格和优惠力度，规格表为此设计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "items_spec")
public class ItemsSpec {
    /**
     * 商品规格id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商品外键id
     */
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 规格名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 折扣力度
     */
    @TableField(value = "discounts")
    private BigDecimal discounts;

    /**
     * 优惠价
     */
    @TableField(value = "price_discount")
    private Integer priceDiscount;

    /**
     * 原价
     */
    @TableField(value = "price_normal")
    private Integer priceNormal;

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