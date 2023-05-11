package com.example.tollcalculator;

import com.example.tollcalculator.domain.Vehicle;
import com.example.tollcalculator.service.TollService;
import com.example.tollcalculator.service.impl.TollServiceImpl;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The TollCalculator class calculates toll fees for a given vehicle and a set of dates representing time of
 * vehicle passing through toll station for the same day.
 * The toll fees are calculated using the TollService implementation provided in the class.
 */
public class TollCalculator {
  /**
   * The TollService implementation used for calculating toll fees.
   */
  private final TollService tollService = new TollServiceImpl();

  /**
   * Calculates the total toll fee for a given vehicle and set of vehicle passes of the same day.
   *
   * @param vehicle the vehicle for which to calculate the toll fee
   * @param passes  the times on which the vehicle passed through a toll station
   * @return the total toll fee for that day for the given vehicle and time of passes.
   */
  public int getTollFee(Vehicle vehicle, Date... passes) {
    List<LocalDateTime> passesInLocalDateTime = convertDates(passes);
    if (!representsSameDate(passesInLocalDateTime)) {
      throw new RuntimeException("Input dates does not represent the same day pass");
    }
    return (int) tollService.calculateTollForPassesOfSameDay(vehicle, passesInLocalDateTime);
  }

  /*
  * Checks if the passes are from the same date.
  * */

  private boolean representsSameDate(final List<LocalDateTime> passes) {
    return passes.stream()
        .map(LocalDateTime::toLocalDate)
        .distinct()
        .count() <= 1;
  }

  /**
   * Converts an array of Date objects to a list of LocalDateTime objects.
   *
   * @param dates the array of Date objects to convert
   * @return a list of LocalDateTime objects representing the same dates as the input array
   */
  private List<LocalDateTime> convertDates(Date[] dates) {
    return Arrays.stream(dates)
        .map(date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
        .collect(Collectors.toList());
  }
}

