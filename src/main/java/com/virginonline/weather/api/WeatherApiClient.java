package com.virginonline.weather.api;

import com.virginonline.weather.model.WeatherResponse;

public interface WeatherApiClient {
  WeatherResponse fetchWeather(String city);
}
