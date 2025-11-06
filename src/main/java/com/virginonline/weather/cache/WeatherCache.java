package com.virginonline.weather.cache;

import com.virginonline.weather.model.WeatherResponse;
import com.virginonline.weather.utils.TimeUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple LRU-like in-memory cache for weather responses with time-to-live (TTL).
 *
 * <p>Stores up to a fixed number of entries and evicts the eldest entry when capacity is exceeded.
 * Each cached item has a timestamp and is considered expired after TTL minutes.
 */
public class WeatherCache {
  private static final Logger log = LoggerFactory.getLogger(WeatherCache.class);

  private static final int MAX_ENTRIES = 10;
  private static final long TTL_MINUTES = 10;
  private final LinkedHashMap<String, CachedWeather> cache =
      new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Entry<String, CachedWeather> eldest) {
          return size() > MAX_ENTRIES;
        }
      };

  public synchronized Optional<WeatherResponse> getCity(String city) {
    var entry = cache.get(city.toLowerCase());
    if (entry == null) {
      return Optional.empty();
    }
    log.debug("Cache lookup for city={} hit={}", city, entry.response());

    if (TimeUtils.isExpired(entry.fetchedAt(), TTL_MINUTES)) {
      cache.remove(city.toLowerCase());
      return Optional.empty();
    }
    return Optional.of(entry.response());
  }

  public synchronized void put(String city, WeatherResponse response) {
    cache.put(city.toLowerCase(), new CachedWeather(response, LocalDateTime.now()));
    log.debug("Cached weather for city={}", city);
  }

  public synchronized Set<String> getStoredCities() {
    return new HashSet<>(cache.keySet());
  }

  public synchronized void clear() {
    cache.clear();
  }

  public synchronized int size() {
    return cache.size();
  }
}
