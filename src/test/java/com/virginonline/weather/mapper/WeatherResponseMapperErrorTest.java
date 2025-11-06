package com.virginonline.weather.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.virginonline.weather.api.mapper.WeatherResponseMapper;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class WeatherResponseMapperErrorTest {

  @Test
  public void testFromJsonMissingNodes() {
    ObjectMapper om = new ObjectMapper();
    ObjectNode root = om.createObjectNode(); // empty
    WeatherResponseMapper mapper = new WeatherResponseMapper();
    assertThrows(Exception.class, () -> mapper.fromJson(root));
  }
}
