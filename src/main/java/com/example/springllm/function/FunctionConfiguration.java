package com.example.springllm.function;

import com.example.springllm.function.WeatherService.Request;
import com.example.springllm.function.WeatherService.Response;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class FunctionConfiguration {

  private final WeatherConfigProperty property;

  public FunctionConfiguration(WeatherConfigProperty property) {
    this.property = property;
  }

  @Bean
  @Description("Get the current weather conditions for the given city.")
  public Function<Request, Response> currentWeatherFunction() {
    return new WeatherService(property);
  }

}
