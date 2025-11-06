package com.virginonline.weather.exception;

/**
 * Base runtime exception for SDK-specific errors.
 *
 * <p>Use or extend this exception to represent non-checked errors occurring inside SDK logic.
 */
public class WeatherException extends RuntimeException {
  public WeatherException() {
    super();
  }

  public WeatherException(String s) {
    super(s);
  }

  public WeatherException(String s, Exception e) {
    super(s, e);
  }
}
