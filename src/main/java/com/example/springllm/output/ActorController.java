package com.example.springllm.output;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/actors")
public class ActorController {

  private final ChatClient chatClient;

  public ActorController(ChatClient.Builder builder) {
    this.chatClient = builder
        .build();
  }

  @GetMapping("/films")
  public ActorFilms getActorFilms() {
    return chatClient.prompt()
        .user("Generate a filmography for a Daniel Day-Lewis.")
        .call()
        .entity(ActorFilms.class);
  }

  @GetMapping("/films-list")
  public List<ActorFilms> listActorFilms() {
    return chatClient.prompt()
        .user("Generate a filmography for the actors Anthony Hopkins, Leonardo DiCaprio and Tom Hanks")
        .call()
        .entity(new ParameterizedTypeReference<>() {});
  }

  @GetMapping("/films-by-actor")
  public ActorFilms getActorFilmsByName(@RequestParam String actor) {
    return chatClient.prompt()
        .user(u -> u.text("Generate a filmography for the actor {actor}").param("actor", actor))
        .call()
        .entity(ActorFilms.class);
  }


}
