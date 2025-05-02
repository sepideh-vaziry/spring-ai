package com.example.springllm.stream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
public class StreamingChatController {

  private final ChatClient chatClient;

  public StreamingChatController(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
        .build();
  }

  @PostMapping("/chat")
  public String chat(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

  @GetMapping("/stream")
  public Flux<String> chatWithStream(@RequestParam String message) {
    return chatClient.prompt()
        .user(message)
        .stream()
        .content();
  }

}
