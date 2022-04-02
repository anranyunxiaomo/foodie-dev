package com.moxuan.constant;

public enum CommonConstant {

    SUCCESS(200, "message"),
    ;

    private Integer code;
    private String message;

    CommonConstant(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
