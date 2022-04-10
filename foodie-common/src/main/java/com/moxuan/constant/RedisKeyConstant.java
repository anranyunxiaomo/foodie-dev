package com.moxuan.constant;

public enum RedisKeyConstant {

    CAROUSEL("轮播图", "carousel"),
    CATS("商品分类", "cats"),
    SUB_CAT("商品分类子分类", "subCat"),
    FOODIE_SHOPCART("购物车", "shopcart"),

    REDIS_USER_TOKEN("用户token 拼接key+userId", "redis_user_token"),

    REDIS_USER_TICKET("用户全局唯一授权票据的拼接key+ticket", "redis_user_ticket"),

    REDIS_USER_TMP_TICKET("用户全局唯一授权临时的票据的拼接key+ticket", "redis_tmp_ticket"),
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
