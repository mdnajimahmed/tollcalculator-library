package com.example.tollcalculator;

import com.example.tollcalculator.domain.Vehicle;
import com.example.tollcalculator.service.TollService;
import com.example.tollcalculator.service.impl.TollServiceImpl;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class TollCalculator {
  private final TollService tollService = new TollServiceImpl();

  public int getTollFee(Vehicle vehicle, Date... dates) {
    return (int) tollService.calculateTollForPasses(vehicle, convertDates(dates));
  }

  private List<LocalDateTime> convertDates(Date[] dates) {
    return Arrays.stream(dates)
        .map(date -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
        .collect(Collectors.toList());
  }

  public static void main(String[] args) {
    System.out.println("Hi");
  }
}
