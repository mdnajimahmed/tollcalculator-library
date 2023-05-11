package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.domain.TollRate;
import com.example.tollcalculator.service.HolidayService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This class provides a holiday service that checks if a given date is a holiday.
 */
public class HolidayServiceImpl implements HolidayService {
  /**
   * A map that contains the holidays as key-value pairs, where the key is the date and the value is the name of the holiday.
   */
  private static Map<LocalDate, String> holidays;

  private static final String HOLIDAYS_FILE_NAME = "holidays.yml";
  private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
      .registerModule(new JavaTimeModule());

  static {
    try (InputStream inputStream = HolidayServiceImpl.class.getResourceAsStream(
        "/" + HOLIDAYS_FILE_NAME)) {
      if (inputStream == null) {
        throw new RuntimeException(
            HOLIDAYS_FILE_NAME + " file not found in the resources directory");
      }
      holidays = objectMapper.readValue(inputStream, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RuntimeException("Failed to read " + HOLIDAYS_FILE_NAME + " file", e);
    }
  }

  /**
   * Constructs a new {@code HolidayServiceImpl} object and initializes the {@code holidays}
   * map with a set of predefined holidays.
   */
  public HolidayServiceImpl() {

  }

  /**
   * Checks if a given date is a holiday.
   *
   * @param date the date to check
   * @return true if the given date is a holiday, false otherwise
   */
  public boolean isHoliday(final LocalDate date) {
    return holidays.containsKey(date);
  }
}