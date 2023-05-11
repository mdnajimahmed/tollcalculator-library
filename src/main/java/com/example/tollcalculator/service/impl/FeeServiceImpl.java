package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.domain.TollRate;
import com.example.tollcalculator.service.FeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.List;

/**
 * Implementation of the {@link FeeService} interface that calculates toll fees based on a set of
 * toll rates defined for each hour of the day.
 * The toll rates are defined as a list of {@link TollRate} objects,
 * each containing an hourly rate for a specific time period.
 */
public class FeeServiceImpl implements FeeService {
  /**
   * A list of toll rates, where each toll rate is defined for a specific time period.
   */
  private static final List<TollRate> tollRates;
  private static final String CONFIG_FILE_NAME = "rates.yml";
  private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
      .registerModule(new JavaTimeModule());

  /**
   Static initialization block that initializes the tollRates list with a set of default toll rates.
   */
  static {
    try (InputStream inputStream = FeeServiceImpl.class.getResourceAsStream(
        "/" + CONFIG_FILE_NAME)) {
      if (inputStream == null) {
        throw new RuntimeException(CONFIG_FILE_NAME + " file not found in the resources directory");
      }
      tollRates = objectMapper.readValue(inputStream,
          objectMapper.getTypeFactory().constructCollectionType(List.class, TollRate.class));
    } catch (IOException e) {
      throw new RuntimeException("Failed to read " + CONFIG_FILE_NAME + " file", e);
    }
  }

  /**
   * Calculates the toll rate for a given time of day, based on the hourly rates defined in the tollRates list.
   *
   * @param pass the time of day for which to calculate the toll rate
   * @return the toll rate for the specified time of day
   */
  @Override
  public double getTollRate(final LocalTime pass) {
    return tollRates
        .stream()
        .filter(r -> r.contains(pass))
        .map(r -> r.getRate())
        .max(Double::compare)
        .orElse(0.0);
  }


}