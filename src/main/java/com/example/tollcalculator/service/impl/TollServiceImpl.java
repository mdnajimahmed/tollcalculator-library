package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.domain.Vehicle;
import com.example.tollcalculator.service.FeeService;
import com.example.tollcalculator.service.HolidayService;
import com.example.tollcalculator.service.TollService;
import com.example.tollcalculator.service.impl.FeeServiceImpl;
import com.example.tollcalculator.service.impl.HolidayServiceImpl;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class TollServiceImpl implements TollService {
  private static final int MINIMUM_MINUTE_BETWEEN_CHARGES = 60;

  public static final int MAXIMUM_FEE_PER_DAY = 60;

  private final FeeService feeService = new FeeServiceImpl();

  private final HolidayService holidayService = new HolidayServiceImpl();

  @Override
  public double calculateTollForPasses(final Vehicle vehicle,
                                       final List<LocalDateTime> passTimestamps) {
    if (isTollFree(vehicle, passTimestamps)) {
      return 0.0;
    }
    final List<List<LocalDateTime>> hourlyWindows = getHourlyWindows(passTimestamps);
    double totalToll = 0;
    for (final List<LocalDateTime> hourlyWindow : hourlyWindows) {
      final double maximumHourlyFee = getMaxHourlyFee(hourlyWindow);
      totalToll += maximumHourlyFee;
      if (totalToll >= MAXIMUM_FEE_PER_DAY) {
        return MAXIMUM_FEE_PER_DAY;
      }
    }
    return totalToll;
  }

  private List<List<LocalDateTime>> getHourlyWindows(final List<LocalDateTime> passTimestamps) {
    final List<List<LocalDateTime>> hourlyWindows = new ArrayList<>();
    List<LocalDateTime> currentHourlyWindow = new ArrayList<>();

    for (final LocalDateTime passTimestamp : passTimestamps) {
      if (currentHourlyWindow.isEmpty() ||
          isWithinHourlyWindow(currentHourlyWindow.get(0), passTimestamp)) {
        currentHourlyWindow.add(passTimestamp);
      } else {
        hourlyWindows.add(currentHourlyWindow);
        currentHourlyWindow = new ArrayList<>();
        currentHourlyWindow.add(passTimestamp);
      }
    }

    if (!currentHourlyWindow.isEmpty()) {
      hourlyWindows.add(currentHourlyWindow);
    }

    return hourlyWindows;
  }

  private double getMaxHourlyFee(final List<LocalDateTime> hourlyWindow) {
    return hourlyWindow.stream()
        .map(LocalDateTime::toLocalTime)
        .mapToDouble(feeService::getTollRate)
        .max()
        .orElse(0.0);
  }

  private boolean isTollFree(final Vehicle vehicle, final List<LocalDateTime> passTimestamps) {
    return vehicle.isTollFree() || passTimestamps.isEmpty() ||
        isTollFreeDay(passTimestamps.get(0).toLocalDate());
  }


  private boolean isWithinHourlyWindow(final LocalDateTime start, final LocalDateTime end) {
    return ChronoUnit.MINUTES.between(start, end) <= MINIMUM_MINUTE_BETWEEN_CHARGES;
  }

  private boolean isTollFreeDay(final LocalDate date) {
    return isWeekend(date) || holidayService.isHoliday(date);
  }

  private boolean isWeekend(final LocalDate toLocalDate) {
    return toLocalDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
        toLocalDate.getDayOfWeek() == DayOfWeek.SUNDAY;
  }

}