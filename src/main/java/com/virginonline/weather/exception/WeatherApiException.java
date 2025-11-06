package com.virginonline.weather.exception;

/**
 * Exception representing failures when communicating with external weather APIs or when parsing
 * their responses.
 */
public class WeatherApiException extends WeatherException {

  public WeatherApiException(String s, Exception e) {
    super(s, e);
  }

  public WeatherApiException(String s) {
    super(s);
  }
}
