package com.moxuan.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 错误码
 *
 * @author zhoupengbing
 */
public enum ResultStatus {

    // -1为通用失败
    FAIL(400, "fail"),
    // 0为成功
    SUCCESS(200, "success"),
    USERNAME_OR_PASSWORD_ERROR(403, "用户名或者密码错误"),
    REPORT_FILE_TYPE_ERROR(2001, "检测报告文件类型错误"),
    REPORT_FILE_UPLOAD_FAIL(2002, "检测报告上传失败"),
    APK_FILE_UPLOAD_FAIL(2003, "应用上传失败"),
    REPORT_IS_TOO_LARGE(2004, "上传的检测报告不能大于100M"),
    FILE_IS_NULL(2005, "上传的文件不能为空"),
    INEFFECTIVE_TIME(2006, "授权日期为无效日期"),
    // --- 4xx Client Error ---
    http_status_bad_request(400, "fail"),
    http_status_unauthorized(401, "Unauthorized"),
    http_status_payment_required(402, "Payment Required"),
    http_status_forbidden(403, "Forbidden"),
    http_status_not_found(404, "Not Found"),
    http_status_method_not_allowed(405, "Method Not Allowed"),
    http_status_not_acceptable(406, "Not Acceptable"),
    http_status_proxy_authentication_required(407, "Proxy Authentication Required"),
    http_status_request_timeout(408, "Request Timeout"),
    http_status_conflict(409, "Conflict"),
    http_status_gone(410, "Gone"),
    http_status_length_required(411, "Length Required"),
    http_status_precondition_failed(412, "Precondition Failed"),
    http_status_payload_too_large(413, "Payload Too Large"),
    http_status_uri_too_long(414, "URI Too Long"),
    http_status_unsupported_media_type(415, "Unsupported Media Type"),
    http_status_requested_range_not_satisfiable(416, "Requested range not satisfiable"),
    http_status_expectation_failed(417, "Expectation Failed"),
    http_status_im_a_teapot(418, "I'm a teapot"),
    http_status_unprocessable_entity(422, "Unprocessable Entity"),
    http_status_locked(423, "Locked"),
    http_status_failed_dependency(424, "Failed Dependency"),
    http_status_upgrade_required(426, "Upgrade Required"),
    http_status_precondition_required(428, "Precondition Required"),
    http_status_too_many_requests(429, "Too Many Requests"),
    http_status_request_header_fields_too_large(431, "Request Header Fields Too Large"),

    // --- 5xx Server Error ---
    http_status_internal_server_error(500, "系统错误"),
    http_status_not_implemented(501, "Not Implemented"),
    http_status_bad_gateway(502, "Bad Gateway"),
    http_status_service_unavailable(503, "Service Unavailable"),
    http_status_gateway_timeout(504, "Gateway Timeout"),
    http_status_http_version_not_supported(505, "HTTP Version not supported"),
    http_status_variant_also_negotiates(506, "Variant Also Negotiates"),
    http_status_insufficient_storage(507, "Insufficient Storage"),
    http_status_loop_detected(508, "Loop Detected"),
    http_status_bandwidth_limit_exceeded(509, "Bandwidth Limit Exceeded"),
    http_status_not_extended(510, "Not Extended"),
    http_status_network_authentication_required(511, "Network Authentication Required"),

    //1000以内是系统错误，
    REQUEST_PARAM_IS_NULL(1000, "请求参数为空"),
    REQUEST_INVALID_PARAM(1001, "请求参数有误"),
    ADMIN_IS_EXISTED(1002, "管理员账号已经存在"),
    UPLOAD_FILE_TOO_LARGE(1010, "上传的文件太大"),
    UPLOAD_FILE_STORE_ERROR(1011, "文件存储异常"),
    USER_IS_FORBIDEN(1401, "用户已经被禁言"),


    /**
     * 佳米错误码   10XX  开头
     */
    JM_CHATROOM_CREATE_ERROR(1001, "创建群组失败"),
    JM_CHATROOM_DELETE_ERROR(1002, "删除群组失败"),
    JM_CHATROOM_UPDATE_ERROR(1003, "更新群组失败"),
    JM_CHATROOM_EXIT_ERROR(1004, "退出群组失败"),
    JM_CHATROOM_COMMON_ERROR(1005, "获取群组列表失败"),

    JM_MESSAGE_SEND_ERROR(1006, "发送自定义消息失败"),
    JM_FS_UPLOAD_ERROR(1007, "上传文件失败"),
    JM_CHATROOM_MESSAGE_SEND_ERROR(1008, "发送群消息失败"),
    JM_ADD_MEMBER_ERROR(1009, "添加人员失败"),
    JM_UPDATE_MEMBER_ERROR(1010, "更新人员失败"),
    JM_SELECT_MEMBER_ERROR(1011, "查询人员失败"),
    JM_DELETE_MEMBER_ERROR(1012, "删除人员失败"),
    JM_EVENT_SEND_ERROR(1013, "发送事件通知失败"),
    JM_CHATROOM_GET_ERROR(1014, "获取群组信息失败"),

    /**
     * 服务内部错误码  11XX 开头
     */
    SYSTEM_SET_MANAGER_USER_NOTHINGNESS_ERROR(1101, "当前成员不存在"),
    SYSTEM_SET_MANAGER_UNABLE_OPERATE_CHATROOM_OWNER_ERROR(1102, "无法对群主进行操作"),
    SYSTEM_CHATROOM_APPLY_ALREADY_EXIST_ERROR(1103, "当前邀请成员已经入群"),
    SYSTEM_CHATROOM_APPLY_CHECK_ALREADY_EXIST_ERROR(1104, "当前成员已经入群"),
    SYSTEM_CHATROOM_APPLY_CHECK_MESSAGE_EXPIRATION_ERROR(1105, "当前消息已过期"),
    SYSTEM_MULTI_UPLOAD_FILE_DATA_EMPTY_ERROR(1106, "上传文件数据为空"),
    SYSTEM_MULTI_UPLOAD_FILE_PARSING_DATA_EXCEPTION_ERROR(1107, "文件解析数据异常"),
    SYSTEM_MULTI_UPLOAD_FILE_SERVER_FAILED(1108, "上传文件服务器失败"),
    //    SYSTEM_SAVE_CHATROOM_AVATAR_FAILED(1109,"生成群头像失败"),
    SYSTEM_SMS_TELEPHONE_ABSENCE_FAILED(1110, "用户无手机号无法发送短信"),
    SYSTEM_MULTI_UPLOAD_AVATAR_PARSING_DATA_EXCEPTION_ERROR(1111, "图片文件解析数据异常"),
    SYSTEM_MULTI_UPLOAD_AVATAR_DATA_EMPTY_ERROR(1112, "上传图片文件数据为空"),
    SYSTEM_CHATROOM_NOT_NULL_ERROR(1113, "无法查询到该群"),
    SYSTEM_ANNUL_MANAGER_NOT_NULL_ERROR(1114, "该成员无法进行撤销群组管理员操作"),

    /**
     * 用户中心错误码  12XX 开头
     */
    CENTER_MEMBER_GET_BATCH_ERROR(1201, "批量获取用户信息失败"),
    CENTER_MEMBER_ALL_ERROR(1202, "获取用户列表失败"),


    /**
     * 用户中心错误码  13XX 开头
     */
    BUSS_SMS_FAILED_ERROR(1301, "短信发送失败"),
    ;


    private static final Logger LOGGER = LoggerFactory.getLogger(ResultStatus.class);

    private final int code;
    private final String msg;

    ResultStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static int getCode(String define) {
        try {
            return ResultStatus.valueOf(define).code;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorCode();
        }
    }

    public static String getMsg(String define) {
        try {
            return ResultStatus.valueOf(define).msg;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorMsg();
        }

    }

    public static String getMsg(int code) {
        for (ResultStatus err : ResultStatus.values()) {
            if (err.code == code) {
                return err.msg;
            }
        }
        return "errorCode not defined ";
    }

    public int getErrorCode() {
        return code;
    }

    public String getErrorMsg() {
        return msg;
    }

}

