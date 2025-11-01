package com.spark.service;

import com.spark.entity.KnowledgeDocument;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/30 17:35
 * @Description
 */
public interface TestService {
    public List<KnowledgeDocument> getList();

    public void saveNewKnow(KnowledgeDocument document);
}
