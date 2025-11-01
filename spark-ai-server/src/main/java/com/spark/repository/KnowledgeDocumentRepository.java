package com.spark.repository;


import com.spark.entity.KnowledgeDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 23:31
 * @Description
 */
public interface KnowledgeDocumentRepository extends ElasticsearchRepository<KnowledgeDocument,String> {
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^2.0\", \"content\", \"keywords\"], \"type\": \"best_fields\"}}")
    List<KnowledgeDocument> semanticSearch(String query, Pageable pageable);

    @Query("{\"match_all\": {}}")
    List<KnowledgeDocument> list();
}
