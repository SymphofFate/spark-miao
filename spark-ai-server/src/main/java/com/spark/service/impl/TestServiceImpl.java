package com.spark.service.impl;

import com.spark.entity.KnowledgeDocument;
import com.spark.repository.KnowledgeDocumentRepository;
import com.spark.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/30 17:36
 * @Description
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private KnowledgeDocumentRepository repository;

    @Override
    public List<KnowledgeDocument> getList() {
        return repository.list();
    }

    @Override
    public void saveNewKnow(KnowledgeDocument document) {
        repository.save(document);
    }
}
