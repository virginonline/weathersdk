package com.virginonline.weather.api;

import com.virginonline.weather.api.mapper.WeatherResponseMapper;
import com.virginonline.weather.exception.WeatherApiException;
import com.virginonline.weather.model.WeatherResponse;
import com.virginonline.weather.utils.HttpUtils;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/** Fetches current weather data from OpenWeather One Call 2.5 API. */
public class OpenWeatherApiClient implements WeatherApiClient {
  private static final Logger log = LoggerFactory.getLogger(OpenWeatherApiClient.class);

  private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
  private final String apiKey;
  private final OkHttpClient httpClient;
  private final ObjectMapper mapper = new ObjectMapper();
  private final WeatherResponseMapper responseMapper;

  public OpenWeatherApiClient(String apiKey) {
    this.apiKey = apiKey;
    this.httpClient =
        new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .callTimeout(java.time.Duration.ofSeconds(10))
            .build();
    this.responseMapper = new WeatherResponseMapper();
  }

  @Override
  public WeatherResponse fetchWeather(String city) throws WeatherApiException {
    try {
      JsonNode json = executeRequest(city);
      return responseMapper.fromJson(json);
    } catch (IOException e) {
      throw new WeatherApiException("Failed to fetch weather for " + city, e);
    }
  }

  /** Responsible only for HTTP call */
  private JsonNode executeRequest(String city) throws IOException, WeatherApiException {
    HttpUrl url =
        HttpUtils.safeUrlBuilder(BASE_URL)
            .addQueryParameter("q", city)
            .addQueryParameter("appid", apiKey)
            .addQueryParameter("units", "metric")
            .build();

    Request request = new Request.Builder().url(url).get().build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        log.warn("Request failed for city={}, code={}", city, response.code());
        throw new WeatherApiException("Weather API returned HTTP " + response.code());
      }
      if (response.body() == null) {
        throw new WeatherApiException("Weather API returned empty body");
      }
      return mapper.readTree(response.body().string());
    }
  }
}
