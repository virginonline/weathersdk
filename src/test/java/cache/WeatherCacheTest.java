package cache;

import static org.junit.jupiter.api.Assertions.*;

import com.virginonline.weather.cache.WeatherCache;
import com.virginonline.weather.model.Temperature;
import com.virginonline.weather.model.WeatherInfo;
import com.virginonline.weather.model.WeatherResponse;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherCacheTest {

  private WeatherCache cache;

  @BeforeEach
  public void setUp() {
    cache = new WeatherCache();
  }

  @Test
  public void testPutAndGetCity() {
    Temperature t = new Temperature(BigDecimal.valueOf(20.0), BigDecimal.valueOf(18.0));
    WeatherInfo info = new WeatherInfo("Clear", "clear sky");
    WeatherResponse resp = new WeatherResponse("London", info, t, 10000, 0L, null, 0);

    cache.put("London", resp);
    Optional<WeatherResponse> opt = cache.getCity("London");
    assertTrue(opt.isPresent());
    assertEquals("London", opt.get().name());
  }

  @Test
  public void testGetCityCaseInsensitiveAndStoredCities() {
    Temperature t = new Temperature(BigDecimal.ZERO, BigDecimal.ZERO);
    WeatherInfo info = new WeatherInfo("X", "x");
    WeatherResponse resp = new WeatherResponse("City", info, t, 0, 0L, null, 0);

    cache.put("TeSt", resp);
    assertTrue(cache.getCity("test").isPresent());
    assertTrue(cache.getStoredCities().contains("test"));
  }

  @Test
  public void testClearAndSize() {
    Temperature t = new Temperature(BigDecimal.ZERO, BigDecimal.ZERO);
    WeatherInfo info = new WeatherInfo("X", "x");
    WeatherResponse resp = new WeatherResponse("C", info, t, 0, 0L, null, 0);

    cache.put("a", resp);
    cache.put("b", resp);
    assertEquals(2, cache.size());
    cache.clear();
    assertEquals(0, cache.size());
  }
}
