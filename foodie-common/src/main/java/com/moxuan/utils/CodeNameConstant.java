package com.moxuan.utils;

public enum CodeNameConstant {

    SUCCESS(
            200, "成功"
    ),
    ERR_METHOD_ARGUMENT_NOT_VALID(
            1, "参数校验无效异常"
    ),
    ERR_BIND(
            2, "绑定异常"
    ),
    ERR_NULL_POINTER(
            3, "空指针异常"
    ),
    ERR_ILLEGAL_ARGUMENT(
            4, "非法参数异常"
    ),
    ERR_ILLEGAL_STATE(
            5, "非法状态异常"
    ),
    ERR_ARITHMETIC(
            6, "计算异常"
    ),
    ERR_CLASS_CAST(
            7, "类型转换异常"
    ),
    ERR_NEGATIVE_ARRAY_SIZE(
            8, "集合负数异常"
    ),
    ERR_ARRAY_INDEX_OUT_OF_BOUNDS(
            9, "集合超出范围异常"
    ),
    ERR_NO_SUCH_METHOD(
            10, "方法未找到异常"
    ),
    ERR_SQL(
            11, "SQL异常"
    ),
    ERR_IO(
            12, "读写异常"
    ),
    ERR_UNSUPPORTED_ENCODING(
            13, "不支持的编码异常"
    ),
    ERR_MAIL_SEND(
            14, "邮件发送异常"
    ),
    ERR_EXCEL_CREATE(
            15, "Excel生成异常"
    ),
    ERR_NO_HANDLER_FOUND(
            21, "找不到处理器异常"
    ),
    ERR_HTTP_REQUEST_METHOD_NOT_SUPPORTED(
            22, "请求方法不支持异常"
    ),
    ERR_HTTP_MEDIA_TYPE_NOT_SUPPORTED(
            23, "请求类型不支持异常"
    ),
    ERR_MISSING_PATH_VARIABLE(
            24, "缺失路径变量异常"
    ),
    ERR_TYPE_MISMATCH(
            25, "类型不匹配异常"
    ),
    ERR_HTTP_MESSAGE_NOT_READABLE(
            26, "HttpMessage不可读异常"
    ),
    ERR_HTTP_MESSAGE_NOT_WRITABLE(
            27, "HttpMessage不可写异常"
    ),
    ERR_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(
            28, "请求类型不接受异常"
    ),
    ERR_REQUEST_BINDING(
            29, "Servlet请求绑定异常"
    ),
    ERR_MISSING_SERVLET_REQUEST_PART(
            30, "缺失Servlet请求异常"
    ),
    ERR_ASYNC_REQUEST_TIMEOUT(
            31, "异步请求超时异常"
    ),
    ERR_NETFLIX_CLIENT(
            40, "服务调用客户端异常"
    ),
    ERR_NETFLIX_RETRYABLE(
            41, "服务调用重试异常"
    ),
    ERR_REST_CLIENT_RESPONSE(
            42, "服务调用重试异常"
    ),
    ERR_NETFLIX_ZUUL(
            43, "网关错误"
    ),
    ERR_UNKNOWN(
            99, "未知错误"
    );


    private final int code;
    private final String msg;

    CodeNameConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(int code) {
        for (CodeNameConstant err : CodeNameConstant.values()) {
            if (err.code == code) {
                return err.msg;
            }
        }
        return ERR_UNKNOWN.msg;
    }
}
