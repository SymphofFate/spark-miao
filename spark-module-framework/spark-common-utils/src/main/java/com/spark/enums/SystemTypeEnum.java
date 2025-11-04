package com.spark.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/3 1:12
 * @Description
 */
@Getter
public enum SystemTypeEnum {

    阿米娅(0,"以明日方舟看板娘阿米娅的语言习惯回答问题"),
    三月七(1,"用《崩坏：星穹铁道》中三月七的口吻回答我的问题"),
    镜流(2,"用《崩坏：星穹铁道》中镜流的口吻回答我的问题");

    private final Integer code;
    private final String systemPrompt;

    SystemTypeEnum(int code, String systemPrompt) {
        this.code = code;
        this.systemPrompt = systemPrompt;
    }

    /**
     * 根据code获取枚举类型
     * @param code 类型代码
     * @return 对应的枚举类型，如果找不到返回null
     */
    public static SystemTypeEnum getType(Integer code) {
        if (code == null) {
            return null;
        }
        for (SystemTypeEnum type : SystemTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据code获取枚举类型，如果找不到抛出异常
     * @param code 类型代码
     * @return 对应的枚举类型
     * @throws IllegalArgumentException 如果code不存在
     */
    public static SystemTypeEnum getTypeStrict(Integer code) {
        SystemTypeEnum type = getType(code);
        if (type == null) {
            throw new IllegalArgumentException("不存在的系统类型代码: " + code);
        }
        return type;
    }

    /**
     * 检查code是否存在
     * @param code 类型代码
     * @return 是否存在
     */
    public static boolean contains(Integer code) {
        return getType(code) != null;
    }

    /**
     * 获取所有有效的code列表
     * @return code列表
     */
    public static List<Integer> getAllCodes() {
        return Arrays.stream(SystemTypeEnum.values())
                .map(SystemTypeEnum::getCode)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有枚举的描述信息
     * @return 描述信息映射
     */
    public static Map<Integer, String> getAllSystemPrompts() {
        Map<Integer, String> prompts = new HashMap<>();
        for (SystemTypeEnum type : SystemTypeEnum.values()) {
            prompts.put(type.getCode(), type.getSystemPrompt());
        }
        return prompts;
    }
}