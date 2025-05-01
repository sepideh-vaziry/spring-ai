package com.example.springllm.function;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat/cities")
public class CityController {

  private final ChatClient chatClient;
  private final WeatherConfigProperty property;

  public CityController(ChatClient.Builder chatClient, WeatherConfigProperty property) {
    this.chatClient = chatClient.build();
    this.property = property;
  }

  @GetMapping
  public String getCities(
      @RequestParam String message
  ) {
    SystemMessage systemMessage = new SystemMessage(
        "You are a helpful AI assistant answering questions about cities around the world."
    );

    UserMessage userMessage = new UserMessage(message);

    OllamaOptions options = OllamaOptions.builder()
        .toolNames("currentWeatherFunction")
        .build();

    return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
        .options(options)
        .call()
        .content();
  }

}
