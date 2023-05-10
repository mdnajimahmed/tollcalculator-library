package com.example.tollcalculator.service;

import java.time.LocalTime;
/**

 Interface for a service that provides the toll rate for a given time of day.
 */

public interface FeeService {
  /**

   Returns the toll rate for a given time of day.
   @param pass the time of day for which the toll rate is being requested.
   @return the toll rate for the given time of day.
   */
  double getTollRate(LocalTime pass);
}
