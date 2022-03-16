package com.moxuan.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultUtil {

    public <T> BaseResp<T> ok(String msg, T data) {
        return BaseResp.create(ResultStatus.SUCCESS.getErrorCode(), msg, data);
    }

    public <T> BaseResp<T> ok(T data) {
        return ok(ResultStatus.SUCCESS.getErrorMsg(), data);
    }

    public <T> BaseResp<T> ok() {
        return ok(null);
    }

    public <T> BaseResp<T> error() {
        return BaseResp.create(ResultStatus.FAIL.getErrorCode(), ResultStatus.FAIL.getErrorMsg(), null);
    }

    public <T> BaseResp<T> error(String msg) {
        return BaseResp.create(ResultStatus.FAIL.getErrorCode(), msg, null);
    }

    public <T> BaseResp<T> common(ResultStatus resultStatus, T data) {
        return BaseResp.create(resultStatus.getErrorCode(), resultStatus.getErrorMsg(), data);
    }

    public <T> BaseResp<T> common(ResultStatus resultStatus) {
        return BaseResp.create(resultStatus.getErrorCode(), resultStatus.getErrorMsg(), null);
    }

    public static BaseResp<Object> common(Integer code, String msg) {
        return BaseResp.create(code, msg, null);
    }

    public static <T> BaseResp<T> common(Integer code, String msg, T data) {
        return BaseResp.create(code, msg, data);
    }
}
