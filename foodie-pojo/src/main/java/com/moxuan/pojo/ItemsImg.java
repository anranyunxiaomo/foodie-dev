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
 * 商品图片
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "items_img")
public class ItemsImg {
    /**
     * 图片主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 商品外键id 商品外键id
     */
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 图片地址 图片地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 顺序 图片顺序，从小到大
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 是否主图 是否主图，1：是，0：否
     */
    @TableField(value = "is_main")
    private Integer isMain;

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