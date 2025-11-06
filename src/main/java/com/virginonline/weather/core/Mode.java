package com.virginonline.weather.core;

/**
 * Operation modes for WeatherSDK.
 *
 * <p>ON_DEMAND - data is fetched only when requested.
 *
 * <p>POOLING - periodic background updates for cached cities are enabled.
 */
public enum Mode {
  ON_DEMAND,
  POOLING
}
