package com.spark.utils;

import com.spark.enums.RequestCodeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    /**
     * 请求成功，无携带内容
     * @return
     */
    public static Result success() {
        return new Result(RequestCodeTypeEnum.SUCCESS.getCode(), RequestCodeTypeEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 请求成功，自定义携带内容
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return new Result(RequestCodeTypeEnum.SUCCESS.getCode(), RequestCodeTypeEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 请求失败，自定义状态码和信息
     * @param code
     * @param message
     * @return
     */
    public static Result failure(Integer code, String message) {
        return new Result(code, message, null);
    }

    /**
     * 请求失败，使用枚举类型
     * @param requestCodeTypeEnum
     * @return
     */
    public static Result failure(RequestCodeTypeEnum requestCodeTypeEnum) {
        return new Result(requestCodeTypeEnum.getCode(), requestCodeTypeEnum.getMessage(), null);
    }
}