package com.example.tollcalculator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FeeService {
  private static final List<TollRate> tollRates;

  static {
    tollRates = new ArrayList<>();
    tollRates.add(new TollRate(LocalTime.of(6, 0), LocalTime.of(6, 30), 8.0));
    tollRates.add(new TollRate(LocalTime.of(6, 30), LocalTime.of(7, 0), 16.0));
    tollRates.add(new TollRate(LocalTime.of(7, 0), LocalTime.of(8, 0), 22.0));
    // add more hourlyFeeRates as needed
  }

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
      return false;
    }
  }
}