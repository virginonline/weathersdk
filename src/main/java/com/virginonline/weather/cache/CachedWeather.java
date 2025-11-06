package com.virginonline.weather.cache;

import com.virginonline.weather.model.WeatherResponse;
import java.time.LocalDateTime;

/**
 * Holder for a cached WeatherResponse together with the fetch timestamp.
 *
 * @param response the weather response object
 * @param fetchedAt timestamp when the response was obtained
 */
public record CachedWeather(WeatherResponse response, LocalDateTime fetchedAt) {}
