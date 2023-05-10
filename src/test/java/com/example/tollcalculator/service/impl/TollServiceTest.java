package com.example.tollcalculator.service.impl;

import com.example.tollcalculator.domain.ForeignServiceVehicle;
import com.example.tollcalculator.domain.PrivateCar;
import com.example.tollcalculator.domain.Vehicle;
import com.example.tollcalculator.service.TollService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class TollServiceTest {

  private static final double EPS = 1E-4;
  private final TollService tollService = new TollServiceImpl();

  @Test
  void testCalculateTollForPassesOnTollFreeVehicle() {
    Vehicle privateCar = new ForeignServiceVehicle();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 10, 7, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicle() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 10, 7, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(18.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnSaturday() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 6, 7, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(0.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnSunday() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 7, 7, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(0.0, fee, EPS);
  }

  // May Day 2023, Monday
  @Test
  void testCalculateTollForPassesOnHoliday() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 1, 7, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(0.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicleMissingFeeEntry() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(LocalDateTime.of(2023, 05, 10, 3, 18, 45));
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicleAndVerifyHourlyPriceCap() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(
        LocalDateTime.of(2023, 05, 10, 15, 25, 0), // 13
        LocalDateTime.of(2023, 05, 10, 16, 15, 0) // 18
    );
    // max(13,18) = 18 is expected result for the same hour period.
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(18.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicleWithMultiplePass() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(
        // Fee : 0 , total fee : 0
        LocalDateTime.of(2023, 05, 10, 3, 25, 0), // 0

        // same hour group , Fee : max(8,13,18) = 18 , total fee : 18
        LocalDateTime.of(2023, 05, 10, 6, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 6, 45, 0),// 13
        LocalDateTime.of(2023, 05, 10, 7, 15, 0), // 18

        // same hour group , Fee : max(18,13,8) = 18 , total fee : 18 + 18 = 36
        LocalDateTime.of(2023, 05, 10, 7, 45, 0), // 18
        LocalDateTime.of(2023, 05, 10, 8, 15, 0), // 13
        LocalDateTime.of(2023, 05, 10, 8, 45, 0), // 8

        // same hour group , Fee : max(8,8,8) = 8 , total fee : 36 + 8 = 44
        LocalDateTime.of(2023, 05, 10, 9, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 9, 45, 0), // 8
        LocalDateTime.of(2023, 05, 10, 10, 15, 0) // 8

    );
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(44.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicleAndVerifyDailyPriceCap() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(
        // Fee : 0 , total fee : 0
        LocalDateTime.of(2023, 05, 10, 3, 25, 0), // 0

        // same hour group , Fee : max(8,13,18) = 18 , total fee : 18
        LocalDateTime.of(2023, 05, 10, 6, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 6, 45, 0),// 13
        LocalDateTime.of(2023, 05, 10, 7, 15, 0), // 18

        // same hour group , Fee : max(18,13,8) = 18 , total fee : 18 + 18 = 36
        LocalDateTime.of(2023, 05, 10, 7, 45, 0), // 18
        LocalDateTime.of(2023, 05, 10, 8, 15, 0), // 13
        LocalDateTime.of(2023, 05, 10, 8, 45, 0), // 8

        // same hour group , Fee : max(8,8,8) = 8 , total fee : 36 + 8 = 44
        LocalDateTime.of(2023, 05, 10, 9, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 9, 45, 0), // 8
        LocalDateTime.of(2023, 05, 10, 10, 15, 0), // 8

        // same hour group , Fee : max(13,18,18) = 18 , total fee : 44 + 18 = 62 , Maxed out!
        LocalDateTime.of(2023, 05, 10, 15, 15, 0), // 13
        LocalDateTime.of(2023, 05, 10, 15, 45, 0), // 18
        LocalDateTime.of(2023, 05, 10, 16, 15, 0) // 18

    );
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(60.0, fee, EPS);
  }

  @Test
  void testCalculateTollForPassesOnTollableVehicleAndVerifyDailyPriceCap2() {
    Vehicle privateCar = new PrivateCar();
    List<LocalDateTime> passes = Arrays.asList(
        // Fee : 0 , total fee : 0
        LocalDateTime.of(2023, 05, 10, 3, 25, 0), // 0

        // same hour group , Fee : max(8,13,18) = 18 , total fee : 18
        LocalDateTime.of(2023, 05, 10, 6, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 6, 45, 0),// 13
        LocalDateTime.of(2023, 05, 10, 7, 15, 0), // 18

        // same hour group , Fee : max(18,13,8) = 18 , total fee : 18 + 18 = 36
        LocalDateTime.of(2023, 05, 10, 7, 45, 0), // 18
        LocalDateTime.of(2023, 05, 10, 8, 15, 0), // 13
        LocalDateTime.of(2023, 05, 10, 8, 45, 0), // 8

        // same hour group , Fee : max(8,8,8) = 8 , total fee : 36 + 8 = 44
        LocalDateTime.of(2023, 05, 10, 9, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 9, 45, 0), // 8
        LocalDateTime.of(2023, 05, 10, 10, 15, 0), // 8

        // same hour group , Fee : max(13,18,18) = 18 , total fee : 44 + 18 = 62 , Maxed out!
        LocalDateTime.of(2023, 05, 10, 15, 15, 0), // 13
        LocalDateTime.of(2023, 05, 10, 15, 45, 0), // 18
        LocalDateTime.of(2023, 05, 10, 16, 15, 0), // 18

        // same hour group
        LocalDateTime.of(2023, 05, 10, 16, 45, 0), //18
        LocalDateTime.of(2023, 05, 10, 17, 15, 0), //13
        LocalDateTime.of(2023, 05, 10, 17, 45, 0), // 13

        // same hour group
        LocalDateTime.of(2023, 05, 10, 18, 15, 0), // 8
        LocalDateTime.of(2023, 05, 10, 18, 45, 0), // 0
        LocalDateTime.of(2023, 05, 10, 19, 15, 0), // 0

        LocalDateTime.of(2023, 05, 10, 19, 45, 0)// 0
    );
    final double fee = tollService.calculateTollForPassesOfSameDay(privateCar, passes);
    Assertions.assertEquals(60.0, fee, EPS);
  }


}