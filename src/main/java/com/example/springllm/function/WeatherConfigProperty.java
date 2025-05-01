package com.example.springllm.function;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
public record WeatherConfigProperty(
    String apiKey,
    String apiUrl
) {

}
