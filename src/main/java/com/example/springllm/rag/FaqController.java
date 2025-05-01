package com.example.springllm.rag;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat/faq")
public class FaqController {

  private final ChatClient chatClient;

  @Value("classpath:/prompts/rag-prompt-template.st")
  private Resource ragPromptTemplate;

  public FaqController(
      ChatClient.Builder builder,
      @Qualifier("olympicVectorStore") VectorStore vectorStore
  ) {
    this.chatClient = builder
        .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,SearchRequest.builder().topK(2).build()))
        .build();
  }

  @GetMapping
  public String faq(@RequestParam(value = "message", defaultValue = "How many athletes compete in the Olympic Games Paris 2024") String message) {
    PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);

    Map<String, Object> modelMap = Map.of(
        "input", message
    );

    Prompt prompt = promptTemplate.create(modelMap);

    return chatClient.prompt(prompt)
        .call()
        .content();
  }

}
