package com.virginonline.weather.utils;

import java.time.Duration;
import java.time.LocalDateTime;

/** Utility methods for time-related operations used by the SDK (TTL checks). */
public class TimeUtils {

  private TimeUtils() {}

  public static boolean isExpired(LocalDateTime localDateTime, long ttlMinutes) {
    return Duration.between(localDateTime, LocalDateTime.now()).toMinutes() > ttlMinutes;
  }
}
