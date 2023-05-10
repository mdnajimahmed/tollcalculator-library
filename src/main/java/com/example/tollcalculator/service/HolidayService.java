package com.example.tollcalculator.service;

import java.time.LocalDate;

/**
 * A service that provides methods for checking if a given date is a holiday.
 */
public interface HolidayService {
  /**
   * Checks if a given date is a holiday.
   *
   * @param date the date to check
   * @return true if the given date is a holiday, false otherwise
   */
  boolean isHoliday(LocalDate date);
}
