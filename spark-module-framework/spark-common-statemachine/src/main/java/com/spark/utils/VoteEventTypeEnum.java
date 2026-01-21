package com.spark.utils;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/29 22:57
 * @Description
 */
public enum VoteEventTypeEnum {
    创建活动(0),
    开始活动(1),
    暂停活动(2),
    结束活动(3);

    private Integer code;

    VoteEventTypeEnum(Integer code) {
        this.code = code;
    }


    public static VoteEventTypeEnum getEnum(Integer code){
        for (VoteEventTypeEnum eventTypeEnum : VoteEventTypeEnum.values()){
            if (eventTypeEnum.code==code){
                return eventTypeEnum;
            }
        }
        return 结束活动;
    }

    public Integer getCode() {
        return code;
    }
}
