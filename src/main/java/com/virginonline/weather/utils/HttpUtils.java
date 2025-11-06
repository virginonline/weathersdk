package com.virginonline.weather.utils;

import com.virginonline.weather.exception.WeatherApiException;
import okhttp3.HttpUrl;

/** HTTP-related utilities used to safely construct request URLs. */
public class HttpUtils {

  private HttpUtils() {}

  public static HttpUrl.Builder safeUrlBuilder(String baseUrl) throws WeatherApiException {
    HttpUrl url = HttpUrl.parse(baseUrl);
    if (url == null) {
      throw new WeatherApiException("Invalid base URL: " + baseUrl);
    }
    return url.newBuilder();
  }
}
