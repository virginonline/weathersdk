package com.virginonline.weather.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class TimeUtilsTest {

  @Test
  public void testIsExpiredFalse() {
    LocalDateTime now = LocalDateTime.now();
    assertFalse(TimeUtils.isExpired(now, 10));
  }

  @Test
  public void testIsExpiredTrue() {
    LocalDateTime past = LocalDateTime.now().minusMinutes(20);
    assertTrue(TimeUtils.isExpired(past, 10));
  }
}
