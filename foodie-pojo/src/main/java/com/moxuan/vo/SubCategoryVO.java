package com.moxuan.vo;

import lombok.Data;

@Data
public class SubCategoryVO {
    /**
     * 主键 分类id
     */
    private Integer subId;

    /**
     * 分类名称 分类名称
     */
    private String subName;

    /**
     * 分类类型 分类得类型，
     * 1:一级大分类
     */
    private Integer subType;

    /**
     * 父id 父id 上一级依赖的id
     */
    private Integer subFatherId;

}
