package com.example.tollcalculator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class TollCalculator {
  private final TollService tollService = new TollService();

  public int getTollFee(Vehicle vehicle, Date... dates) {
    return (int) tollService.calculateTollForPasses(vehicle, Arrays.stream(dates)
        .map(date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
        .collect(Collectors.toList()));
  }

  public static void main(String[] args) {
    System.out.println("Hi");
  }
}
