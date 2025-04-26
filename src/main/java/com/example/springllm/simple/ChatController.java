package com.example.springllm.simple;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class ChatController {

  private final OpenAiChatModel chatModel;

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

}
