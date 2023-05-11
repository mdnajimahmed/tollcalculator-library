package com.example.tollcalculator.domain;

import java.time.LocalTime;

/**
 * Represents a toll rate for a specific time period, containing an hourly rate for that period.
 */

public class TollRate {
  public TollRate() {
  }

  /*
      The start time of the time period for which the toll rate applies.
    */
  private LocalTime startTime;
  /*
   * The end time of the time period for which the toll rate applies.
   */

  private LocalTime endTime;
  /**
   * The hourly toll rate for the time period.
   */
  private double rate;

  /**
   * Creates a new TollRate object with the specified start and end times and hourly toll rate.
   *
   * @param hourStart the start time of the time period for which the toll rate applies
   * @param hourEnd   the end time of the time period for which the toll rate applies
   */
  TollRate(final LocalTime hourStart, final LocalTime hourEnd, final double fee) {
    this.startTime = hourStart;
    this.endTime = hourEnd;
    this.rate = fee;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public double getRate() {
    return rate;
  }

  /**
   * Checks whether the given time is contained within the interval of this TollRate object.
   *
   * @param time the LocalTime object to be checked for containment within this TollRate object's interval
   * @return true if the given time is within the interval of this TollRate object, false otherwise
   * @throws IllegalArgumentException if the given time is null
   */

  public boolean contains(final LocalTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Null value is not allowed");
    }
    return !time.isBefore(startTime) && !time.isAfter(endTime);

  }
}