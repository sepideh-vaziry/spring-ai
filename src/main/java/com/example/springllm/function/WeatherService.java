package com.example.springllm.function;

import com.example.springllm.function.WeatherService.Request;
import com.example.springllm.function.WeatherService.Response;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeatherService implements Function<Request, Response> {

  private final WeatherConfigProperty weatherConfigProperty;
  private final RestClient restClient;

  public WeatherService(WeatherConfigProperty weatherConfigProperty) {
    this.weatherConfigProperty = weatherConfigProperty;
    this.restClient = RestClient.create(weatherConfigProperty.apiUrl());
  }

  @Override
  public Response apply(Request request) {
    log.info("Weather Request: {}", request);

    Response response = restClient.get()
        .uri("/current.json?key={key}&q={q}", weatherConfigProperty.apiKey(), request.city())
        .retrieve()
        .body(Response.class);

    log.info("Weather API Response: {}", response);

    return response;
  }

  public record Request(String city) {}
  public record Response(Location location, Current current) {}
  public record Location(String name, String region, String country, Long lat, Long lon) {}
  public record Current(String temp_f, Condition condition, String wind_mph, String humidity) {}
  public record Condition(String text) {}
}
