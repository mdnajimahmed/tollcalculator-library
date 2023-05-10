package com.example.tollcalculator.service;

import java.time.LocalDate;

public interface HolidayService {
  boolean isHoliday(LocalDate date);
}
