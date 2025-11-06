package com.virginonline.weather.core;

import com.virginonline.weather.api.OpenWeatherApiClient;
import com.virginonline.weather.api.WeatherApiClient;
import com.virginonline.weather.cache.WeatherCache;
import com.virginonline.weather.exception.WeatherApiException;
import com.virginonline.weather.model.WeatherResponse;
import com.virginonline.weather.updater.WeatherUpdater;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherSDK {
  private static final Logger log = LoggerFactory.getLogger(WeatherSDK.class);

  private final String apiKey;
  private final Mode mode;
  private final WeatherCache cache;
  private final WeatherApiClient apiClient;
  private final ScheduledExecutorService scheduler;

  private WeatherUpdater updater;
  private static final int POOLING_INTERVAL_MINUTES = 1;

  public WeatherSDK(String apiKey, Mode mode) {
    this.apiKey = apiKey;
    this.mode = mode;
    this.cache = new WeatherCache();
    this.apiClient = new OpenWeatherApiClient(apiKey);
    this.scheduler = Executors.newSingleThreadScheduledExecutor();
    if (mode == Mode.POOLING) {
      startPoolingUpdater();
    }
    log.info("Initializing com.virginonline.weather.core.WeatherSDK with mode={}", mode);
  }

  public WeatherSDK(String apiKey) {
    this(apiKey, Mode.ON_DEMAND);
  }

  public synchronized WeatherResponse getWeather(String city) throws WeatherApiException {
    var cached = cache.getCity(city);
    if (cached.isPresent()) return cached.get();

    WeatherResponse response = apiClient.fetchWeather(city);
    cache.put(city, response);
    return response;
  }

  private void startPoolingUpdater() {
    updater = new WeatherUpdater(cache, apiClient);
    scheduler.scheduleAtFixedRate(
        updater::updateAll, 0, POOLING_INTERVAL_MINUTES, TimeUnit.MINUTES);
  }

  public void delete() {
    if (scheduler != null && !scheduler.isShutdown()) {
      scheduler.shutdown();
    }
    if (mode == Mode.POOLING && updater != null) {
      updater.stop();
    }
    log.info("Shutting down WeatherSDK instance");

    WeatherSDKFactory.deleteInstance(apiKey);
  }

  public String getApiKey() {
    return apiKey;
  }

  public Mode getMode() {
    return mode;
  }
}
