package com.example.tollcalculator;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tollcalculator.domain.PrivateCar;
import com.example.tollcalculator.domain.Vehicle;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TollCalculatorTest {
  private static final double EPS = 1E-4;
  private final TollCalculator tollCalculator = new TollCalculator();

  @Test
  void testTollCalculation() {
    Vehicle privateCar = new PrivateCar();
    Date[] passes = new Date[] {
        // Fee : 0 , total fee : 0
        new GregorianCalendar(2023, 9, 3, 3, 25, 0).getTime(),

        // same hour group , Fee : max(8,13,18) = 18 , total fee : 18
        new GregorianCalendar(2023, 9, 3, 6, 15, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 6, 45, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 7, 15, 0).getTime(),

        // same hour group , Fee : max(18,13,8) = 18 , total fee : 18 + 18 = 36
        new GregorianCalendar(2023, 9, 3, 7, 45, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 8, 15, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 8, 45, 0).getTime(),

        // same hour group , Fee : max(8,8,8) = 8 , total fee : 36 + 8 = 44
        new GregorianCalendar(2023, 9, 3, 9, 15, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 9, 45, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 10, 15, 0).getTime(),

        // same hour group , Fee : max(13,18,18) = 18 , total fee : 44 + 18 = 62 , Maxed out!
        new GregorianCalendar(2023, 9, 3, 15, 15, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 15, 45, 0).getTime(),
        new GregorianCalendar(2023, 9, 3, 16, 15, 0).getTime()
    };
    final int fee = tollCalculator.getTollFee(privateCar, passes);
    Assertions.assertEquals(60, fee);
  }

  @Test
  void testTollCalculationMultipleDate() {
    Vehicle privateCar = new PrivateCar();
    Date[] passes = new Date[] {
        new GregorianCalendar(2023, 9, 3, 3, 25, 0).getTime(),
        new GregorianCalendar(2023, 9, 4, 6, 15, 0).getTime()
    };
    Exception exception = assertThrows(RuntimeException.class, () -> {
      tollCalculator.getTollFee(privateCar, passes);
    });
    Assertions.assertNotNull(exception);
    String expectedMessage = "Input dates does not represent the same day pass";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testTollCalculationMultipleEmptyPass() {
    Vehicle privateCar = new PrivateCar();
    Date[] passes = new Date[] {};
    final int fee = tollCalculator.getTollFee(privateCar, passes);
    Assertions.assertEquals(0, fee);
  }

}