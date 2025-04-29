package com.example.springllm.rag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

@Command
public class SpringAssistantCommand {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;

  @Value("classpath:/prompts/spring-boot-reference.st")
  private Resource sbPromptTemplate;

  public SpringAssistantCommand(ChatClient.Builder builder, VectorStore vectorStore) {
    this.vectorStore = vectorStore;

    this.chatClient = builder
        .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,SearchRequest.builder().topK(2).build()))
        .build();
  }

  @Command(command = "q")
  public String question(@DefaultValue(value = "What is Spring Boot") String message) {
    PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);

    Map<String, Object> promptParameters = new HashMap<>();
    promptParameters.put("input", message);
    promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));

    return chatClient.prompt(promptTemplate.create(promptParameters))
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
