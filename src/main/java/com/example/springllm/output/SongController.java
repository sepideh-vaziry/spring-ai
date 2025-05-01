package com.example.springllm.output;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class SongController {

  private final ChatModel chatModel;

  @GetMapping("/songs")
  public List<String> findPopularSongs(
      @RequestParam(defaultValue = "Taylor Swift") String artist
  ) {
    String message = """
        Please give me a list of top 10 song for the artist {artist}. If you don't know the answer, just say "I don't know.".
        {format}
        """;

    ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

    Map<String, Object> modelMap = Map.of(
        "artist", artist,
        "format", listOutputConverter.getFormat()
    );

    PromptTemplate promptTemplate = new PromptTemplate(message, modelMap);
    Prompt prompt = promptTemplate.create();

    ChatResponse chatResponse = chatModel.call(prompt);

    return listOutputConverter.convert(chatResponse.getResult().getOutput().getText());
  }

}
