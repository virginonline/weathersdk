package com.virginonline.weather.model;

public record WeatherResponse(
    String name,
    WeatherInfo weather,
    Temperature temperature,
    int visibility,
    long datetime,
    Sys sys,
    int timezone) {}
