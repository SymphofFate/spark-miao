package com.spark.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/22 22:30
 * @Description
 */
public enum FileTypeEnum {

    JPEG("jpeg","image/jpg;charset=UTF-8"),
    JPG("jpg","image/jpg;charset=UTF-8"),
    PNG("png","image/jpg;charset=UTF-8"),
    GIF("gif","image/jpg;charset=UTF-8"),
    BMP("bmp","image/jpg;charset=UTF-8"),
    WEBP("webp","image/jpg;charset=UTF-8"),
    PDF("pdf","application/pdf"),
    MP4("mp4","video/mp4"),
    WEBM("webm","video/webm"),
    OGG("ogg","video/ogg"),
    OGV("ogv","video/ogg"),
    AVI("avi","video/x-msvideo"),
    MP3("mp3","audio/mpeg"),
    DEFAULT("","application/octet-stream"),
    ;

    private String fileType;
    private String contentType;

    FileTypeEnum(String fileType, String contentType) {
        this.fileType = fileType;
        this.contentType = contentType;
    }

    public static String getContentType(String fileType){
        if (StrUtil.isBlank(fileType)){
            return DEFAULT.contentType;
        }
        for (FileTypeEnum typeEnum : FileTypeEnum.values()){
            if (typeEnum.fileType.equals(fileType)){
                return typeEnum.contentType;
            }
        }
        return DEFAULT.contentType;
    }
}
