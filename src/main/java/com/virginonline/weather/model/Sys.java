package com.virginonline.weather.model;

/**
 * System data containing sunrise and sunset times.
 *
 * @param sunrise epoch seconds for sunrise
 * @param sunset epoch seconds for sunset
 */
public record Sys(Long sunrise, Long sunset) {}
