package com.virginonline.weather.api;

import com.virginonline.weather.model.WeatherResponse;

/**
 * Abstraction over a weather API client used by the SDK to fetch current weather data.
 *
 * <p>Implementations should perform HTTP requests to a remote provider and return a parsed
 * WeatherResponse or throw an appropriate runtime exception on failures.
 */
public interface WeatherApiClient {
  WeatherResponse fetchWeather(String city);
}
