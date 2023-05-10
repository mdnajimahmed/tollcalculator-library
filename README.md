# Problem statement:
https://github.com/EvolveTechnology/toll-calculator

# Explanation on the approach:
- The existing API is kept un-changes since there might be other systems depending on the original API. 
```
/**
   * Calculate the total toll fee for one day
   *
   * @param vehicle - the vehicle
   * @param dates   - date and time of all passes on one day
   * @return - the total toll fee for that day
   */
  public int getTollFee(Vehicle vehicle, Date... dates) {
``` 
However, here are some improvements done on the original API
- As per the documentation, the original API assumes the dates provided will be of same day however, it does not validate that or throws any error if otherwise. This solution implements that validation and returns and error if the dates are not of the same day.
- This solution internally uses the new Java 8 date APIs instead of using java.util.Date which makes the solution robust and easy to maintain in future since many java.util.Date related APIs are deprecated or on the way to phase out.
- Unlike the original solution, the core business logics are segregated into three services
  - FeeService - Responsible for fee related logics. It has one a public method `getTollRate` that returns amount of toll be paid based on the time of the day. The implementation details and internal representation of toll rate is hidden from the client following OOP encapsulation principle. 
  - HolidayService - Responsible to manage holidays. It has one public method `isHoliday` that takes an instance of localdate and returns true if the day is a holiday. The implementation details and internal representation of holiday calendar is hidden from the client following OOP encapsulation principle.
  - TollService - With the help of FeeService and HolidayService it calculates toll for the given vehicle and passes.
- Toll Calculation logic:
  - At first, we check do we have to pay any toll at all based on vehicle type and date. We have a abstract base class `Vehicle` which has a method `isTollFree()`, all the logic is written on abstraction(Vehicle) following OOP best practice. The concrete implementations of vehicle provide implementation for  `isTollFree()` denoting if the vehicle should pay toll based on it's concrete type. In this way we eliminated the enum of tollable vehicles which makes the code more maintainable in future following `Open-Close` principle.
  - After that we group those vehicles together that falls under same hour period. We sort the dates and run a sliding window algorithm to do it in O(nlog(n)) complexity. In this way, we fix the hourly calculation bug in the existing code that only compares with the first vehicle's pass time while doing hourly grouping. 
  - Then for each hour group, we take the maximum payable amount among all the passes within that group and add that amount with our final result.Since, we are only taking the max for each hour period, it also ensures that the vehicle pays only once within an hour window. 
  - We keep adding until the daily total goes beyond 60SEK. 
  - If the daily total goes beyond 60SEK we stop further calculation and return 60SEK, otherwise we return the total amount for the day.
  - Here is an example from the unit test written for TollService to illustrate the logic further with an example
  - ```
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(
    // Fee : 0 , total fee : 0 (No entry toll rate at 325 am midnight in the fee table , this means it's a toll free time!)
    LocalDateTime.of(2023, 05, 10, 3, 25, 0), // toll to pay for this pass = 0
      
      // same hour group , Fee : max(8,13,18) = 18 , total fee : 0 + 18 = 18
      LocalDateTime.of(2023, 05, 10, 6, 15, 0), // toll to pay for this pass = 8
      LocalDateTime.of(2023, 05, 10, 6, 45, 0),// toll to pay for this pass = 13
      LocalDateTime.of(2023, 05, 10, 7, 15, 0), // toll to pay for this pass = 18

      // same hour group , Fee : max(18,13,8) = 18 , total fee : 18 + 18 = 36
      LocalDateTime.of(2023, 05, 10, 7, 45, 0), // toll to pay for this pass = 18
      LocalDateTime.of(2023, 05, 10, 8, 15, 0), // toll to pay for this pass = 13
      LocalDateTime.of(2023, 05, 10, 8, 45, 0), // toll to pay for this pass = 8

      // same hour group , Fee : max(8,8,8) = 8 , total fee : 36 + 8 = 44
      LocalDateTime.of(2023, 05, 10, 9, 15, 0), // toll to pay for this pass = 8
      LocalDateTime.of(2023, 05, 10, 9, 45, 0), // toll to pay for this pass = 8
      LocalDateTime.of(2023, 05, 10, 10, 15, 0), // toll to pay for this pass = 8

      // same hour group , Fee : max(13,18,18) = 18 , total fee : 44 + 18 = 62 , Maxed out! return 60.
      LocalDateTime.of(2023, 05, 10, 15, 15, 0), // toll to pay for this pass = 13
      LocalDateTime.of(2023, 05, 10, 15, 45, 0), // toll to pay for this pass = 18
      LocalDateTime.of(2023, 05, 10, 16, 15, 0), // toll to pay for this pass = 18
          
      // ---- Does not process these subsequent entries ----
      // same hour group
      LocalDateTime.of(2023, 05, 10, 16, 45, 0), // toll to pay for this pass = 18
      LocalDateTime.of(2023, 05, 10, 17, 15, 0), // toll to pay for this pass = 13
      LocalDateTime.of(2023, 05, 10, 17, 45, 0), // toll to pay for this pass = 13

      // same hour group
      LocalDateTime.of(2023, 05, 10, 18, 15, 0), // toll to pay for this pass = 8
      LocalDateTime.of(2023, 05, 10, 18, 45, 0), // toll to pay for this pass = 0
      LocalDateTime.of(2023, 05, 10, 19, 15, 0), // toll to pay for this pass = 0

      LocalDateTime.of(2023, 05, 10, 19, 45, 0) // toll to pay for this pass = 0
    );
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(60.0, fee, EPS);
  ```
- In the future, we may want to process multiple passes of multiple hence, hence the service API is felxible to take localdatetime instead of just localtime.