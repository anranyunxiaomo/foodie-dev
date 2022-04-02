package com.moxuan.constant;

public enum ErrorInfoConstant {

    SUB_CAT_CATEGORY_DOES_NOT_EXIST("分类不存在"),


    ;


    private String errorInfo;


    ErrorInfoConstant(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }


}
