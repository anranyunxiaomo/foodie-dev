package com.moxuan.constant;


public enum BasicConstant {

    /**
     * 分类类型 分类得类型，
     * 1:一级大分类
     * 2:二级分类
     * 3:三级小分类
     */
    CATEGORY_PRIMARY(1, "一级大分类"),
    CATEGORY_SECONDARY(2, "二级分类"),
    CATEGORY_TERTIARY(3, "三级小分类"),

    ;


    private Integer type;

    private String description;


    BasicConstant(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
