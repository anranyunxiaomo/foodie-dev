package com.moxuan.constant;

public enum RedisKeyConstant {

    CAROUSEL("轮播图", "carousel"),
    CATS("商品分类", "cats"),
    SUB_CAT("商品分类子分类", "subCat"),
    FOODIE_SHOPCART("购物车", "shopcart"),

    REDIS_USER_TOKEN ("用户token","redis_user_token"),
    ;


    private String description;
    private String name;

    RedisKeyConstant(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }


}
