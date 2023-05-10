package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.service.FeeService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link FeeService} interface that calculates toll fees based on a set of
 * toll rates defined for each hour of the day.
 * The toll rates are defined as a list of {@link TollRate} objects,
 * each containing an hourly rate for a specific time period.
 */
public class FeeServiceImpl implements FeeService {
  /**
   * A list of toll rates, where each toll rate is defined for a specific time period.
   */
  private static final List<TollRate> tollRates;

  /**
   Static initialization block that initializes the tollRates list with a set of default toll rates.
   */
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

  /**
   * Calculates the toll rate for a given time of day, based on the hourly rates defined in the tollRates list.
   *
   * @param pass the time of day for which to calculate the toll rate
   * @return the toll rate for the specified time of day
   */
  @Override
  public double getTollRate(final LocalTime pass) {
    return tollRates
        .stream()
        .filter(r -> r.contains(pass))
        .map(r -> r.fee)
        .max(Double::compare)
        .orElse(0.0);
  }

  /**
   * Represents a toll rate for a specific time period, containing an hourly rate for that period.
   */
  static class TollRate {
    /*
The start time of the time period for which the toll rate applies.
*/
    private LocalTime hourStart;
    /*
     * The end time of the time period for which the toll rate applies.
     */

    private LocalTime hourEnd;
    /**
     * The hourly toll rate for the time period.
     */
    private double fee;

    /**
     * Creates a new TollRate object with the specified start and end times and hourly toll rate.
     *
     * @param hourStart the start time of the time period for which the toll rate applies
     * @param hourEnd   the end time of the time period for which the toll rate applies
     */
    TollRate(final LocalTime hourStart, final LocalTime hourEnd, final double fee) {
      this.hourStart = hourStart;
      this.hourEnd = hourEnd;
      this.fee = fee;
    }

    LocalTime getHourStart() {
      return hourStart;
    }

    LocalTime getHourEnd() {
      return hourEnd;
    }

    double getFee() {
      return fee;
    }

    /**
     * Checks whether the given time is contained within the interval of this TollRate object.
     *
     * @param time the LocalTime object to be checked for containment within this TollRate object's interval
     * @return true if the given time is within the interval of this TollRate object, false otherwise
     * @throws IllegalArgumentException if the given time is null
     */

    boolean contains(final LocalTime time) {
      if (time == null) {
        throw new IllegalArgumentException("Null value is not allowed");
      }
      return !time.isBefore(hourStart) && !time.isAfter(hourEnd);

    }
  }
}