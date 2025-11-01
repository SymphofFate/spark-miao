package com.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 23:16
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "knowledge_base")
public class KnowledgeDocument {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.Keyword)
    private String source;
    @Field(type = FieldType.Dense_Vector, dims = 1536)
    private float[] embedding;
    @Field(type = FieldType.Float)
    private Float score;
    @Field(type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second_fraction, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
