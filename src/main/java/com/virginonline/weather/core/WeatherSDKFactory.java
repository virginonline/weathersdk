package com.virginonline.weather.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating and managing WeatherSDK instances.
 *
 * <p>This factory implements a singleton pattern with support for multiple instances differentiated
 * by API key. It provides thread-safe operations for creating and deleting WeatherSDK instances.
 *
 * <p>Example usage:
 *
 * <pre>
 *     // Create/get a WeatherSDK instance
 *     WeatherSDK sdk = WeatherSDKFactory.getInstance("your-api-key", Mode.POOLING);
 *
 *     // Delete an instance
 *     WeatherSDKFactory.deleteInstance("your-api-key");
 * </pre>
 *
 * @see WeatherSDK
 * @see Mode
 */
public class WeatherSDKFactory {

  private static final ConcurrentHashMap<String, WeatherSDK> instances = new ConcurrentHashMap<>();

  public static synchronized WeatherSDK getInstance(String apiKey, Mode mode) {
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalArgumentException("API key must not be null or empty");
    }
    return instances.computeIfAbsent(apiKey, k -> new WeatherSDK(k, mode));
  }

  public static synchronized void deleteInstance(String apiKey) {
    instances.remove(apiKey);
  }
}
