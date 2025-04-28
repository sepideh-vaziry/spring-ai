package com.example.springllm.rag;

import groovyjarjarpicocli.CommandLine.Command;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Command
public class SpringAssistantCommand {

  private final ChatClient chatClient;
  private final VectorStore vectorStore;

  @Value("classpath:/prompts/spring-boot-reference.st")
  private Resource sbPromptTemplate;

  public SpringAssistantCommand(ChatClient chatClient, VectorStore vectorStore) {
    this.chatClient = chatClient;
    this.vectorStore = vectorStore;
  }

//  @Command(command = "q")
//  public String question(@DefaultValue(value = "What is Spring Boot") String message) {
//    PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
//    Map<String, Object> promptParameters = new HashMap<>();
//    promptParameters.put("input", message);
//    promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));
//
//    return chatClient.call(promptTemplate.create(promptParameters))
//        .getResult()
//        .getOutput()
//        .getContent();
//  }
//
//  private List<String> findSimilarDocuments(String message) {
//    List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
//    return similarDocuments.stream().map(Document::getContent).toList();
//  }

}
