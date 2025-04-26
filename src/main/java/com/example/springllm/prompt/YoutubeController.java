package com.example.springllm.prompt;

import java.util.Map;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/ai/chat")
public class YoutubeController {

  private final OpenAiChatModel chatModel;

  @Value("classpath:/prompts/youtube.st")
  private Resource youtubePromptResource;

  @GetMapping("/youtube/popular")
  public String findPopularYoutubers(
      @RequestParam(defaultValue = "tech") String genre
  ) {
    PromptTemplate promptTemplate = new PromptTemplate(youtubePromptResource);
    Prompt prompt = promptTemplate.create(Map.of("genre", genre));

    return chatModel.call(prompt).getResult().getOutput().getText();
  }

}
