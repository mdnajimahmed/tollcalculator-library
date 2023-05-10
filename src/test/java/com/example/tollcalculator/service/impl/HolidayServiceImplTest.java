package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.service.HolidayService;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HolidayServiceImplTest {
  private final HolidayService holidayService = new HolidayServiceImpl();
  @Test
  void isMayDayAHoliday() {
    boolean isHoliday = holidayService.isHoliday(LocalDate.of(2023, 5, 1));
    Assertions.assertTrue(isHoliday);
  }

  @Test
  void isMaySecondAHoliday() {
    boolean isHoliday = holidayService.isHoliday(LocalDate.of(2023, 5, 2));
    Assertions.assertFalse(isHoliday);
  }
}