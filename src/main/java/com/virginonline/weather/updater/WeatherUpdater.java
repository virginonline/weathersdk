package com.virginonline.weather.updater;

import com.virginonline.weather.api.WeatherApiClient;
import com.virginonline.weather.cache.WeatherCache;
import com.virginonline.weather.model.WeatherResponse;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scheduled component that updates cached weather data for all stored cities.
 *
 * <p>The updater iterates over city keys in WeatherCache, fetches fresh data via the provided
 * {@link WeatherApiClient} and replaces cache entries. It can be stopped via {@link #stop()} to
 * cancel further updates.
 */
public class WeatherUpdater {

  private final WeatherCache cache;
  private final WeatherApiClient client;
  private final AtomicBoolean running = new AtomicBoolean(true);
  private static final Logger log = LoggerFactory.getLogger(WeatherUpdater.class);

  public WeatherUpdater(WeatherCache cache, WeatherApiClient client) {
    this.cache = cache;
    this.client = client;
  }

  public void updateAll() {
    if (!running.get()) {
      return;
    }
    log.info("Starting scheduled weather update for {} cached cities", cache.size());

    for (String city : cache.getStoredCities()) {
      try {
        WeatherResponse response = client.fetchWeather(city);
        cache.put(city, response);
      } catch (Exception e) {
        log.error("Error updating weather for city: {}", city, e);
      }
      log.info("Weather update complete");
    }
  }

  public void stop() {
    running.set(false);
  }
}
