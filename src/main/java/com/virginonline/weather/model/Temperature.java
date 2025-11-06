package com.virginonline.weather.model;

import java.math.BigDecimal;

/**
 * Temperature values container.
 *
 * @param temp current temperature in degrees Celsius
 * @param feelsLike perceived temperature in degrees Celsius
 */
public record Temperature(BigDecimal temp, BigDecimal feelsLike) {}
