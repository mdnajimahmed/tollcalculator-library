package com.example.tollcalculator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TollService {
  public static final int MAXIMUM_FEE_PER_DAY = 60;
  private final FeeService feeService = new FeeService();
  private final HolidayService holidayService = new HolidayService();

  public double calculate(final List<LocalDateTime> passes) {
    return passes
        .stream()
        .sorted()
        .collect(Collectors.groupingBy(LocalDateTime::toLocalDate))
        .values()
        .stream()
        .map(this::calculateTollForSameDayPasses)
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  private double calculateTollForSameDayPasses(final List<LocalDateTime> passes) {
    if (holidayService.isHoliday(passes.get(0).toLocalDate())) {
      return 0;
    }
    final double totalBilled = passes.stream()
        .collect(Collectors.groupingBy(dt ->
            dt.withMinute(0).withSecond(0).withNano(0)))
        .values()
        .stream()
        .map(this::calculateTollForHourPasses)
        .mapToDouble(Double::doubleValue)
        .sum();
    return Math.min(totalBilled, MAXIMUM_FEE_PER_DAY);

  }

  private double calculateTollForHourPasses(final List<LocalDateTime> localDateTimes) {
    return localDateTimes.stream().map(feeService::getTollRate).max(Double::compare).orElse(0.0);
  }
}
