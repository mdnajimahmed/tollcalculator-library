package com.example.tollcalculator.service.impl;


import com.example.tollcalculator.service.FeeService;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeeServiceImplTest {
  private static final double EPS = 1E-4;
  private final FeeService feeService = new FeeServiceImpl();

  @Test
  void getTollRateForBeforeTheMinimumHourConfigured() {
    double fee = feeService.getTollRate(LocalTime.of(3, 0, 0));
    Assertions.assertEquals(fee, 0, EPS);
  }

  @Test
  void getTollRateOnStartOfAnInternal() {
    double fee = feeService.getTollRate(LocalTime.of(8, 30, 0));
    Assertions.assertEquals(fee, 8, EPS);
  }

  @Test
  void getTollRateInBetweenOfAnInternal() {
    double fee = feeService.getTollRate(LocalTime.of(11, 30, 0));
    Assertions.assertEquals(fee, 8, EPS);
  }

  @Test
  void getTollRateOnEndOfAnInternal() {
    double fee = feeService.getTollRate(LocalTime.of(14, 59, 59));
    Assertions.assertEquals(fee, 8, EPS);
  }

  @Test
  void getTollRateForAfterTheMaximumHourConfigured() {
    double fee = feeService.getTollRate(LocalTime.of(23, 0, 0));
    Assertions.assertEquals(fee, 0, EPS);
  }
}