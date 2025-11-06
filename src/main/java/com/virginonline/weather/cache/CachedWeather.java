package com.virginonline.weather.cache;

import com.virginonline.weather.model.WeatherResponse;
import java.time.LocalDateTime;

public record CachedWeather(WeatherResponse response, LocalDateTime fetchedAt) {}
