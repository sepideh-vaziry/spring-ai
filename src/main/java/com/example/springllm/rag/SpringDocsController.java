package com.example.springllm.rag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat/spring-docs")
public class SpringDocsController {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;

  @Value("classpath:/prompts/spring-boot-reference.st")
  private Resource sbPromptTemplate;

  public SpringDocsController(ChatClient.Builder builder, VectorStore vectorStore) {
    this.vectorStore = vectorStore;
    this.chatClient = builder
        .build();
  }

  @GetMapping
  public String faq(@RequestParam(
      value = "message", defaultValue = "What is RestClient in Spring Boot?") String message
  ) {
    PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);

    Map<String, Object> promptParameters = new HashMap<>();
    promptParameters.put("input", message);
    promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));
    Prompt prompt = promptTemplate.create(promptParameters);

    return chatClient.prompt(prompt)
        .call()
        .content();
  }

  private List<String> findSimilarDocuments(String message) {
    List<Document> similarDocuments = vectorStore.similaritySearch(
        new SearchRequest.Builder()
            .query(message)
            .topK(3)
            .build()
    );

    return similarDocuments.stream().map(Document::getFormattedContent).toList();
  }

}
