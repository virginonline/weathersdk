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

/**
 * Main SDK entry point for fetching and caching weather data.
 *
 * <p>This class provides the primary interface for interacting with weather data services. It
 * supports two operational modes: ON_DEMAND for fetching weather data when requested, and POOLING
 * for automatically refreshing weather data at regular intervals.
 *
 * <p>The SDK manages an in-memory cache to improve performance and reduce API calls. In POOLING
 * mode, a background scheduler automatically updates cached weather information.
 *
 * <p>Usage example:
 *
 * <pre>
 * WeatherSDK sdk = new WeatherSDK("your-api-key");
 * WeatherResponse weather = sdk.getWeather("London");
 * </pre>
 *
 * @see Mode for operational modes
 * @see WeatherCache for caching mechanism
 * @see WeatherApiClient for API interactions
 */
public class WeatherSDK {
  private static final Logger log = LoggerFactory.getLogger(WeatherSDK.class);

  private final String apiKey;
  private final Mode mode;
  private final WeatherCache cache;
  private final WeatherApiClient apiClient;
  private final ScheduledExecutorService scheduler;

  private WeatherUpdater updater;
  private final Integer POOLING_INTERVAL_MINUTES;

  public WeatherSDK(String apiKey, Mode mode, Integer poolingIntervalMinutes) {
    this.apiKey = apiKey;
    this.mode = mode;
    this.cache = new WeatherCache();
    this.POOLING_INTERVAL_MINUTES = poolingIntervalMinutes;
    this.apiClient = new OpenWeatherApiClient(apiKey);
    this.scheduler = Executors.newSingleThreadScheduledExecutor();
    if (mode == Mode.POOLING) {
      startPoolingUpdater();
    }
    log.info("Initializing com.virginonline.weather.core.WeatherSDK with mode={}", mode);
  }

  public WeatherSDK(String apiKey) {
    this(apiKey, Mode.ON_DEMAND, 10);
  }

  public WeatherSDK(String apiKey, Mode mode) {
    this(apiKey, mode, 10);
  }

  public synchronized WeatherResponse getWeather(String city) throws WeatherApiException {
    var cached = cache.getCity(city);
    if (cached.isPresent()) return cached.get();

    WeatherResponse response = apiClient.fetchWeather(city);
    cache.put(city, response);
    return response;
  }

  /**
   * Starts the polling updater for weather data in POOLING mode.
   *
   * <p>This method initializes a WeatherUpdater instance with the current cache and API client,
   * then schedules it to run at fixed intervals. The updater will automatically refresh weather
   * data for all cached cities every {@link #POOLING_INTERVAL_MINUTES} minutes.
   *
   * <p>The scheduled task begins execution immediately (initial delay of 0) and continues running
   * at the specified interval until the SDK is shut down.
   *
   * @see WeatherUpdater
   * @see WeatherCache
   * @see WeatherApiClient
   * @see #delete()
   */
  private void startPoolingUpdater() {
    updater = new WeatherUpdater(cache, apiClient);
    scheduler.scheduleAtFixedRate(
        updater::updateAll, 0, POOLING_INTERVAL_MINUTES, TimeUnit.MINUTES);
  }

  /**
   * Shuts down the WeatherSDK instance and releases associated resources.
   *
   * <p>This method performs cleanup operations including:
   *
   * <ul>
   *   <li>Shutting down the scheduled executor service if it's running
   *   <li>Stopping the weather updater in POOLING mode
   *   <li>Removing this instance from the factory's instance map
   * </ul>
   *
   * <p>This method should be called when the SDK is no longer needed to prevent resource leaks and
   * ensure proper cleanup of background threads.
   *
   * @see WeatherUpdater#stop()
   * @see WeatherSDKFactory#deleteInstance(String)
   */
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
