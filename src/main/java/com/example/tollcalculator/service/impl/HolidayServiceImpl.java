package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.service.HolidayService;
import java.time.LocalDate;
import java.util.Map;

/**
 * This class provides a holiday service that checks if a given date is a holiday.
 */
public class HolidayServiceImpl implements HolidayService {
  /**
   * A map that contains the holidays as key-value pairs, where the key is the date and the value is the name of the holiday.
   */
  private final Map<LocalDate, String> holidays;

  /**
   * Constructs a new {@code HolidayServiceImpl} object and initializes the {@code holidays}
   * map with a set of predefined holidays.
   */
  public HolidayServiceImpl() {
    holidays = Map.ofEntries(
        Map.entry(LocalDate.of(2023, 1, 1), "New Year's Day"),
        Map.entry(LocalDate.of(2023, 1, 6), "Epiphany"),
        Map.entry(LocalDate.of(2023, 4, 14), "Good Friday"),
        Map.entry(LocalDate.of(2023, 4, 16), "Easter Sunday"),
        Map.entry(LocalDate.of(2023, 4, 17), "Easter Monday"),
        Map.entry(LocalDate.of(2023, 5, 1), "May Day"),
        Map.entry(LocalDate.of(2023, 5, 25), "Ascension Day"),
        Map.entry(LocalDate.of(2023, 6, 4), "Whit Sunday"),
        Map.entry(LocalDate.of(2023, 6, 6), "National Day"),
        Map.entry(LocalDate.of(2023, 6, 9), "Whit Monday"),
        Map.entry(LocalDate.of(2023, 12, 24), "Christmas Eve"),
        Map.entry(LocalDate.of(2023, 12, 25), "Christmas Day"),
        Map.entry(LocalDate.of(2023, 12, 26), "Boxing Day"),
        Map.entry(LocalDate.of(2023, 12, 31), "New Year's Eve")
    );
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