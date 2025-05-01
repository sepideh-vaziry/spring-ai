package com.example.springllm.image;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/image/describe")
public class ImageDescriptorController {

  private final ChatClient chatClient;

  public ImageDescriptorController(ChatClient.Builder chatClient) {
    this.chatClient = chatClient.build();
  }

  @GetMapping
  public String describeImage() {
    ClassPathResource resource = new ClassPathResource("images/describe-image.jpg");

    UserMessage userMessage = new UserMessage(
        "Can you please explain what you see in following image.",
        new Media(MimeTypeUtils.IMAGE_JPEG, resource)
    );

    return chatClient.prompt(new Prompt(userMessage))
        .call()
        .content();
  }

}
