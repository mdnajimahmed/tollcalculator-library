package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.domain.Vehicle;
import com.example.tollcalculator.service.FeeService;
import com.example.tollcalculator.service.HolidayService;
import com.example.tollcalculator.service.TollService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class implements the {@link TollService} interface and provides methods for calculating toll fees
 * for passes of the same day by a {@link Vehicle} at certain times. It utilizes a {@link FeeService} to calculate
 * the toll rates for each pass and a {@link HolidayService} to check whether a pass occurred on a toll-free day.
 */
public class TollServiceImpl implements TollService {
  /**
   * The minimum number of minutes between two consecutive toll charges.
   */
  private static final int MINIMUM_MINUTE_BETWEEN_CHARGES = 60;
  /**
   * The maximum fee per day that can be charged for tolls.
   */
  public static final int MAXIMUM_FEE_PER_DAY = 60;
  /**
   * A {@link FeeService} used to calculate toll rates for each pass.
   */
  private final FeeService feeService = new FeeServiceImpl();
  /**
   * A {@link HolidayService} used to check whether a pass occurred on a toll-free day.
   */
  private final HolidayService holidayService = new HolidayServiceImpl();

  /**
   * Calculates the total toll fee for passes of the same day by a {@link Vehicle} at certain times.
   *
   * @param vehicle        the {@link Vehicle} making the passes.
   * @param passTimestamps the times of the passes.
   * @return the total toll fee for the passes.
   */
  @Override
  public double calculateTollForPassesOfSameDay(final Vehicle vehicle,
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

  /**
   * Divides a list of pass times into lists of times that fall within the same hour.
   *
   * @param passTimestamps the times of the passes.
   * @return a list of hourly windows, each containing pass times that fall within the same hour.
   */

  private List<List<LocalDateTime>> getHourlyWindows(final List<LocalDateTime> passTimestamps) {
    Collections.sort(passTimestamps);
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

  /**
   * Calculates the maximum toll rate for a list of pass times.
   *
   * @param hourlyWindow the times of the passes in a particular hourly window.
   * @return the maximum toll rate for the hourly window.
   */
  private double getMaxHourlyFee(final List<LocalDateTime> hourlyWindow) {
    return hourlyWindow.stream()
        .map(LocalDateTime::toLocalTime)
        .mapToDouble(feeService::getTollRate)
        .max()
        .orElse(0.0);
  }

  /**
   * Checks if vehicle should pay toll at all depending on the type of vehicle and the day of pass.
   *
   * @param vehicle        the vehicle to check
   * @param passTimestamps the list of pass timestamps for the vehicle
   * @return true if the vehicle is toll-free or there are no passes for the vehicle or the first pass was on a toll-free day,
   * false otherwise
   */
  private boolean isTollFree(final Vehicle vehicle, final List<LocalDateTime> passTimestamps) {
    return vehicle.isTollFree() || passTimestamps.isEmpty() ||
        isTollFreeDay(passTimestamps.get(0).toLocalDate());
  }

  /**
   * Checks if the given end timestamp is within the minimum number of minutes between toll charges from the given start timestamp.
   *
   * @param start the start timestamp
   * @param end   the end timestamp
   * @return true if the end timestamp is within the minimum number of minutes between toll charges from the start timestamp, false otherwise
   */
  private boolean isWithinHourlyWindow(final LocalDateTime start, final LocalDateTime end) {
    return ChronoUnit.MINUTES.between(start, end) <= MINIMUM_MINUTE_BETWEEN_CHARGES;
  }

  /**
   * Checks if the given date is a toll-free day.
   *
   * @param date the date to check
   * @return true if the date is a toll-free day (weekend or holiday), false otherwise
   */
  private boolean isTollFreeDay(final LocalDate date) {
    return isWeekend(date) || holidayService.isHoliday(date);
  }

  /**
   * Checks if the given date is a weekend day (Saturday or Sunday).
   *
   * @param date the date to check
   * @return true if the date is a weekend day (Saturday or Sunday), false otherwise
   */
  private boolean isWeekend(final LocalDate date) {
    return date.getDayOfWeek() == DayOfWeek.SATURDAY ||
        date.getDayOfWeek() == DayOfWeek.SUNDAY;
  }

}