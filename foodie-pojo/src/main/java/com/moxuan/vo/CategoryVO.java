package com.moxuan.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {

    /**
     * 主键 分类id
     */
    private Integer id;

    /**
     * 分类名称 分类名称
     */
    private String name;

    /**
     * 分类类型 分类得类型，
     * 1:一级大分类
     */
    private Integer type;

    /**
     * 父id 父id 上一级依赖的id
     */
    private Integer fatherId;

    private List<SubCategoryVO> subCatList;

}
