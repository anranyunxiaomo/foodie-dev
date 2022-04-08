package com.moxuan.constant;

public enum CookieKeyConstant {
    HEADER_USER_ID("cookie请求头中用户的id的key","headerUserId"),
    HEADER_USER_TOKEN("cookie请求头中用户的Token的key","headerUserToken"),
    ;

    CookieKeyConstant(String description, String name) {
        this.description = description;
        this.name = name;
    }

    private String description;
    private String name;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
