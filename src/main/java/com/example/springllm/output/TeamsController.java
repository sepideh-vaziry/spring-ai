package com.example.springllm.output;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/nba/teams")
public class TeamsController {

  private final ChatClient chatClient;

  public TeamsController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping
  public List<NbaTeam> teams() {
    return chatClient.prompt()
        .user("Please give the name all of the teams in the NBA.")
        .call()
        .entity(new ParameterizedTypeReference<List<NbaTeam>>() {});
  }

}
