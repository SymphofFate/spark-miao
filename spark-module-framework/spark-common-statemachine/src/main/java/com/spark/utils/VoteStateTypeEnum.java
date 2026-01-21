package com.spark.utils;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/11/29 22:47
 * @Description
 */
public enum VoteStateTypeEnum {

    未创建(-1,"活动未创建"),
    未开始(0,"未开始"),
    进行中(1,"活动进行中"),
    暂停(2,"活动暂停"),
    结束(3,"活动结束");

    private Integer code;
    private String stateType;

    VoteStateTypeEnum(Integer code, String stateType) {
        this.code = code;
        this.stateType = stateType;
    }

    public String getStateType() {
        return stateType;
    }

    public Integer getCode(){
        return code;
    }

    public static VoteStateTypeEnum getEnum(Integer code){
        for (VoteStateTypeEnum stateTypeEnum : VoteStateTypeEnum.values()){
            if (stateTypeEnum.code==code){
                return stateTypeEnum;
            }
        }
        return null;
    }
}
