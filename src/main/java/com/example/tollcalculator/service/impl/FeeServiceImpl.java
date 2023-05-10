package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.service.FeeService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FeeServiceImpl implements FeeService {
  private static final List<TollRate> tollRates;

  static {
    tollRates = new ArrayList<>();
    tollRates.add(new TollRate(LocalTime.of(6, 0, 0), LocalTime.of(6, 29, 59), 8.0));
    tollRates.add(new TollRate(LocalTime.of(6, 30, 0), LocalTime.of(6, 59, 59), 13.0));
    tollRates.add(new TollRate(LocalTime.of(7, 0, 0), LocalTime.of(7, 59, 59), 18.0));
    tollRates.add(new TollRate(LocalTime.of(8, 0, 0), LocalTime.of(8, 29, 59), 13.0));
    tollRates.add(new TollRate(LocalTime.of(8, 30, 0), LocalTime.of(14, 59, 59), 8.0));
    tollRates.add(new TollRate(LocalTime.of(15, 0, 0), LocalTime.of(15, 29, 59), 13.0));
    tollRates.add(new TollRate(LocalTime.of(15, 30, 0), LocalTime.of(16, 59, 59), 18.0));
    tollRates.add(new TollRate(LocalTime.of(17, 0, 0), LocalTime.of(17, 59, 59), 13.0));
    tollRates.add(new TollRate(LocalTime.of(18, 0, 0), LocalTime.of(18, 29, 59), 8.0));
    // add more hourlyFeeRates as needed
  }

  @Override
  public Double getTollRate(final LocalTime pass) {
    return tollRates
        .stream()
        .filter(r -> r.contains(pass))
        .map(r -> r.fee)
        .max(Double::compare)
        .orElse(0.0);
  }

  public static class TollRate {
    private LocalTime hourStart;
    private LocalTime hourEnd;
    private double fee;

    public TollRate(final LocalTime hourStart, final LocalTime hourEnd, final double fee) {
      this.hourStart = hourStart;
      this.hourEnd = hourEnd;
      this.fee = fee;
    }

    public LocalTime getHourStart() {
      return hourStart;
    }

    public LocalTime getHourEnd() {
      return hourEnd;
    }

    public double getFee() {
      return fee;
    }

    public boolean contains(final LocalTime time) {
      if (time == null) {
        throw new IllegalArgumentException("Null value is not allowed");
      }
      return !time.isBefore(hourStart) && !time.isAfter(hourEnd);

    }
  }
}