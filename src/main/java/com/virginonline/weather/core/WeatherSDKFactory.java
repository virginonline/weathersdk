package com.virginonline.weather.core;

import java.util.concurrent.ConcurrentHashMap;

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
