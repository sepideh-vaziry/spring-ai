package com.example.springllm.stuff;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat/olympics")
public class OlympicController {

  private final OpenAiChatModel chatModel;

  @Value("classpath:/prompts/olympic-sports.st")
  private Resource olympicSportsResource;

  @Value("classpath:/docs/olympic-sports.txt")
  private Resource docToStuffResource;

  @GetMapping("/2024")
  public String findPopularSongs(
      @RequestParam(defaultValue = "true") boolean stuffIt
  ) {
    String message = "What sports are being include in the 2024 summer Olympics?";

    PromptTemplate promptTemplate = new PromptTemplate(olympicSportsResource);

    Map<String, Object> modelMap = Map.of(
        "question", message,
        "context", stuffIt ? docToStuffResource : ""
    );

    Prompt prompt = promptTemplate.create(modelMap);

    ChatResponse chatResponse = chatModel.call(prompt);

    return chatResponse.getResult().getOutput().getText();
  }

}
