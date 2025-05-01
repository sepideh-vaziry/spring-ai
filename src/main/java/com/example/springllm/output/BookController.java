package com.example.springllm.output;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat/books")
public class BookController {

  private final ChatClient chatModel;

  public BookController(ChatClient.Builder chatModel) {
    this.chatModel = chatModel.build();
  }

  @GetMapping("/author/{author}")
  public Map<String, Object> getAuthorsSocialLinks(
      @PathVariable String author
  ) {
    String message = """
        Generate a list of links for the author {author}.
        Include the authors name as the key any social network links as the object.
        {format}
        """;

    MapOutputConverter mapOutputConverter = new MapOutputConverter();

    Map<String, Object> modelMap = Map.of(
        "author", author,
        "format", mapOutputConverter.getFormat()
    );

    PromptTemplate promptTemplate = new PromptTemplate(message, modelMap);
    Prompt prompt = promptTemplate.create();

    ChatResponse chatResponse = chatModel.prompt(prompt)
        .call()
        .chatResponse();

    return mapOutputConverter.convert(chatResponse.getResult().getOutput().getText());
  }

  @GetMapping("/by-author")
  public Author getBooksByAuthor(
      @RequestParam(defaultValue = "Agatha Christie") String author
  ) {
    String message = """
        Generate a list of links for the author {author}.
        Include the authors name as the key any social network links as the object.
        {format}
        """;

    var beanOutputConverter = new BeanOutputConverter<>(Author.class);

    Map<String, Object> modelMap = Map.of(
        "author", author,
        "format", beanOutputConverter.getFormat()
    );

    PromptTemplate promptTemplate = new PromptTemplate(message, modelMap);
    Prompt prompt = promptTemplate.create();

    ChatResponse chatResponse = chatModel.prompt(prompt)
        .call()
        .chatResponse();

    return beanOutputConverter.convert(chatResponse.getResult().getOutput().getText());
  }

}
