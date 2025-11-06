package com.virginonline.weather.api.mapper;

import com.virginonline.weather.exception.WeatherApiException;
import com.virginonline.weather.model.Sys;
import com.virginonline.weather.model.Temperature;
import com.virginonline.weather.model.WeatherInfo;
import com.virginonline.weather.model.WeatherResponse;
import com.virginonline.weather.model.Wind;
import tools.jackson.databind.JsonNode;

public class WeatherResponseMapper {

  public WeatherResponse fromJson(JsonNode json) throws WeatherApiException {
    try {
      JsonNode weatherNode = json.path("weather").get(0);
      JsonNode mainNode = json.path("main");
      JsonNode sysNode = json.path("sys");
      JsonNode windNode = json.path("wind");

      WeatherInfo weather =
          new WeatherInfo(
              weatherNode.path("main").asString("Unknown"),
              weatherNode.path("description").asString("No description"));

      Temperature temperature =
          new Temperature(
              mainNode.path("temp").asDecimal(), mainNode.path("feels_like").asDecimal());

      Wind wind = new Wind(windNode.path("speed").asDecimal());

      Sys sys = new Sys(sysNode.path("sunrise").asLong(0L), sysNode.path("sunset").asLong(0L));

      int visibility = json.path("visibility").asInt(0);
      long datetime = json.path("dt").asLong(0);
      int timezone = json.path("timezone").asInt(0);
      String name = json.path("name").asString("Unknown");

      return new WeatherResponse(name, weather, temperature, visibility, datetime, sys, timezone);

    } catch (Exception e) {
      throw new WeatherApiException("Failed to parse weather response", e);
    }
  }
}
