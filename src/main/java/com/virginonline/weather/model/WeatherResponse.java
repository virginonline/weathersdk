package com.virginonline.weather.model;

/**
 * Data transfer object representing parsed weather information for a location.
 *
 * @param name location name
 * @param weather brief weather description and main condition
 * @param temperature temperature-related values (current, feels-like)
 * @param visibility visibility in meters
 * @param datetime epoch seconds of the data calculation
 * @param sys system information (sunrise/sunset)
 * @param timezone shift in seconds from UTC for the location
 */
public record WeatherResponse(
    String name,
    WeatherInfo weather,
    Temperature temperature,
    int visibility,
    Wind wind,
    long datetime,
    Sys sys,
    int timezone) {}
