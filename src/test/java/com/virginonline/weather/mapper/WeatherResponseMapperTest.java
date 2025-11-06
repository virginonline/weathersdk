package com.virginonline.weather.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.virginonline.weather.api.mapper.WeatherResponseMapper;
import com.virginonline.weather.model.WeatherResponse;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

public class WeatherResponseMapperTest {

  @Test
  public void testFromJson() throws Exception {
    ObjectNode root = getJsonNodes();

    WeatherResponseMapper mapper = new WeatherResponseMapper();
    WeatherResponse resp = mapper.fromJson(root);

    assertNotNull(resp);
    assertEquals("TestCity", resp.name());
    // temperature
    assertEquals(new BigDecimal("15.5"), resp.temperature().temp());
    assertEquals(new BigDecimal("14.0"), resp.temperature().feelsLike());
    // weather info
    assertEquals("Clear", resp.weather().main());
    assertEquals("clear sky", resp.weather().description());
    // other fields
    assertEquals(10000, resp.visibility());
    assertEquals(3600, resp.timezone());
    assertEquals(1L, resp.sys().sunrise());
    assertEquals(2L, resp.sys().sunset());
  }

  @NotNull
  private static ObjectNode getJsonNodes() {
    ObjectMapper om = new ObjectMapper();
    ObjectNode root = om.createObjectNode();
    ArrayNode weatherArr = om.createArrayNode();
    ObjectNode weather = om.createObjectNode();
    weather.put("main", "Clear");
    weather.put("description", "clear sky");
    weatherArr.add(weather);
    root.set("weather", weatherArr);

    ObjectNode main = om.createObjectNode();
    main.put("temp", 15.5);
    main.put("feels_like", 14.0);
    root.set("main", main);

    ObjectNode wind = om.createObjectNode();
    wind.put("speed", 3.2);
    root.set("wind", wind);

    ObjectNode sys = om.createObjectNode();
    sys.put("sunrise", 1L);
    sys.put("sunset", 2L);
    root.set("sys", sys);

    root.put("visibility", 10000);
    root.put("dt", 1000L);
    root.put("timezone", 3600);
    root.put("name", "TestCity");
    return root;
  }
}
