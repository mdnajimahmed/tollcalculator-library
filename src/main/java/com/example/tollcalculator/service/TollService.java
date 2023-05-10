package com.example.tollcalculator.service;

import com.example.tollcalculator.domain.Vehicle;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for a toll service that calculates toll charges for a given vehicle and list of pass timestamps.
 */

public interface TollService {
  /**
   * Calculates the total toll for passes of the same day for the given vehicle and list of pass timestamps.
   * If the vehicle is toll-free or the passes are on a toll-free day, the toll will be zero.
   * If the total toll exceeds the maximum fee per day, the maximum fee will be returned instead.
   *
   * @param vehicle        the vehicle for which to calculate the toll
   * @param passTimestamps the list of pass timestamps for the vehicle
   * @return the total toll for passes of the same day
   */
  double calculateTollForPassesOfSameDay(Vehicle vehicle, List<LocalDateTime> passTimestamps);
}
