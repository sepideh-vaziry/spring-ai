package com.example.aitutorial;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
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
public class ChatController {

  private final OpenAiChatModel chatModel;

  @Value("classpath:/prompts/youtube.st")
  private Resource youtubePromptResource;

  @GetMapping("/generate-joke")
  public String generateJoke() {
//    return chatModel.call("Tell me a joke");

    var systemMessage = new SystemMessage("""
        Your primary function is to tell a joke. If someone asks you for any other things please tell them you only tell joke.
        """);
    var userMessage = new UserMessage("Tell me a joke");
    Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

    return chatModel.call(prompt).getResult().getOutput().getText();
  }

  @GetMapping("/youtube/popular")
  public String findPopularYoutubers(
      @RequestParam(defaultValue = "tech") String genre
  ) {
    PromptTemplate promptTemplate = new PromptTemplate(youtubePromptResource);
    Prompt prompt = promptTemplate.create(Map.of("genre", genre));

    return chatModel.call(prompt).getResult().getOutput().getText();
  }

}
