package com.virginonline.weather.model;

/**
 * Brief weather information describing the main weather condition and a textual description.
 *
 * @param main short weather category (e.g. "Clouds", "Clear")
 * @param description human-readable description
 */
public record WeatherInfo(String main, String description) {}
