package com.spark.service.impl;
import com.spark.entity.KnowledgeDocument;
import com.spark.repository.KnowledgeDocumentRepository;
import com.spark.service.ElasticsearchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 23:14
 * @Description
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Resource
    private KnowledgeDocumentRepository repository;


    /**
     * 语义搜索相关文档
     */
    public List<KnowledgeDocument> semanticSearch(String query, int maxResults) {
        try {
            // 构建搜索请求
            Pageable pageable = PageRequest.of(0, maxResults);
            List<KnowledgeDocument> results = repository.semanticSearch(query, pageable);
            return results;
        } catch (Exception e) {
            log.error("ES搜索失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public void initKnowledgeIndex() {
        //先清库
        removeAll();
        List<KnowledgeDocument> list = new ArrayList<>();
        KnowledgeDocument knowledgeDocument = new KnowledgeDocument();
        knowledgeDocument.setTitle("邢俊辉");
        knowledgeDocument.setSource("22岁");
        knowledgeDocument.setContent("一个失业的程序员");
        knowledgeDocument.setScore(2f);
        list.add(knowledgeDocument);
        repository.saveAll(list);
    }

    @Override
    public void saveList(List<KnowledgeDocument> documents) {
        repository.saveAll(documents);
    }

    public void removeAll(){
        repository.deleteAll();
    }
}
