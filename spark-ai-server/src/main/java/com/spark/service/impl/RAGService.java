package com.spark.service.impl;

import com.spark.entity.KnowledgeDocument;
import com.spark.service.ElasticsearchService;
import com.spark.utils.SystemTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author <a href="https://gitee.com/a-tom-is-cry">Xing</a>
 * @Date 2025/10/2 23:19
 * @Description
 */
@Service
@Slf4j
public class RAGService {

    @Resource
    private ElasticsearchService elasticsearchService;

    @Resource
    private ChatClient dashScopeChatClient;

    // 系统提示词模板 - 包含检索到的上下文
    private static final String RAG_SYSTEM_PROMPT = """
        {user}
        参考信息：
        {context}
        """;

    /**
     * 基于知识库的智能问答
     */
    public String askWithKnowledgeBase(Integer code,String question, String conversationId) {
        // 1. 从ES检索相关文档
        List<KnowledgeDocument> relevantDocs = elasticsearchService.semanticSearch(question, 5);
        String systemPrompt;
        if (relevantDocs.isEmpty()) {
            // 3. 构建包含上下文的系统提示词
            systemPrompt = RAG_SYSTEM_PROMPT.replace("{context}", "")
                    .replace("{user}", SystemTypeEnum.getType(code).getSystemPrompt());
        }else {
            // 2. 构建上下文
            String context = buildContextFromDocuments(relevantDocs);
            // 3. 构建包含上下文的系统提示词
            systemPrompt = RAG_SYSTEM_PROMPT.replace("{context}", context)
                    .replace("{user}", SystemTypeEnum.getType(code).getSystemPrompt());
        }
        // 4. 调用AI生成回答
        return generateAnswerWithContext(systemPrompt, question, conversationId);
    }

    /**
     * 从检索到的文档构建上下文
     */
    private String buildContextFromDocuments(List<KnowledgeDocument> documents) {
        StringBuilder context = new StringBuilder();

        for (int i = 0; i < documents.size(); i++) {
            KnowledgeDocument doc = documents.get(i);
            context.append(String.format("【文档%d - 相关性: %.2f】\n", i + 1, doc.getScore()));
            context.append("标题: ").append(doc.getTitle()).append("\n");
            context.append("内容: ").append(doc.getContent()).append("\n");
            context.append("来源: ").append(doc.getSource()).append("\n\n");
        }

        return context.toString();
    }

    /**
     * 调用AI生成带上下文的回答
     */
    private String generateAnswerWithContext(String systemPrompt, String question, String conversationId) {
        try {
            // 使用流式响应获取答案
            StringBuffer answer = new StringBuffer();

            dashScopeChatClient.prompt()
                    .system(systemPrompt)
                    .user(question)
                    .advisors(a -> a.param("conversationId", conversationId)
                            .param("retrieveSize", 10)) // 限制使用最近10条历史记录
                    .stream()
                    .content()
                    .toStream()
                    .forEach(content -> {
                        answer.append(content);
                        // 这里可以添加流式输出的逻辑
                        System.out.print(content);
                    });

            return answer.toString();

        } catch (Exception e) {
            log.error("AI回答生成失败: {}", e.getMessage(), e);
            return "抱歉，生成回答时出现错误，请稍后重试。";
        }
    }

    /**
     * 批量导入知识文档
     */
    public void importKnowledgeDocuments(List<KnowledgeDocument> documents) {
        elasticsearchService.saveList(documents);
    }
}
