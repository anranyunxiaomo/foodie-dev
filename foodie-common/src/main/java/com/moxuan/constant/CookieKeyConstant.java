package com.moxuan.constant;

public enum CookieKeyConstant {
    HEADER_USER_ID("cookie请求头中用户的id的key", "headerUserId"),

    HEADER_USER_TOKEN("cookie请求头中用户的Token的key", "headerUserToken"),

    COOKIE_USER_TICKET("cookie请求头中用户的全局唯一登陆凭证/票据", "cookie_user_ticket"),

    COOKIE_DOMAIN("cookie 设置当前用户的唯一凭证能够使用的域名下", "sso.com"),
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
