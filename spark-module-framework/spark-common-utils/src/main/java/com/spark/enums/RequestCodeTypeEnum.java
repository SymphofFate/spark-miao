package com.spark.enums;

public enum RequestCodeTypeEnum {
    SUCCESS(200, "请求成功"),
    NOT_LOGGED_IN(401, "未登录"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源未找到"),
    FAILURE(500, "服务器异常");

    private final Integer code;
    private final String message;

    RequestCodeTypeEnum(Integer code, String message) {
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
