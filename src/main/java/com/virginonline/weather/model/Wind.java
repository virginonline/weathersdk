package com.virginonline.weather.model;

import java.math.BigDecimal;

/**
 * Wind data holder.
 *
 * @param speed wind speed in meters/second
 */
public record Wind(BigDecimal speed) {}
