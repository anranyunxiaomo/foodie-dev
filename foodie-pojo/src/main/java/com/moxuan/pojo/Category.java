package com.moxuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品分类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "category")
public class Category {
    /**
     * 主键 分类id主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 分类名称 分类名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 分类类型 分类得类型，
     * 1:一级大分类
     * 2:二级分类
     * 3:三级小分类
     */
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     */
    @TableField(value = "father_id")
    private Integer fatherId;

    /**
     * 图标 logo
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 口号
     */
    @TableField(value = "slogan")
    private String slogan;

    /**
     * 分类图
     */
    @TableField(value = "cat_image")
    private String catImage;

    /**
     * 背景颜色
     */
    @TableField(value = "bg_color")
    private String bgColor;
}