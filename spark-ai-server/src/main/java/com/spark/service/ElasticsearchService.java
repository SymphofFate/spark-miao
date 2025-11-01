package com.spark.service;

import com.spark.entity.KnowledgeDocument;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 23:14
 * @Description
 */
public interface ElasticsearchService {
    public List<KnowledgeDocument> semanticSearch(String query, int maxResults);

    void initKnowledgeIndex();

    void saveList(List<KnowledgeDocument> documents);
}
