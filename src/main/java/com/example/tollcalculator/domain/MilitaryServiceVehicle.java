package com.example.tollcalculator.domain;

public class MilitaryServiceVehicle extends Vehicle{
  @Override
  public boolean isTollFree() {
    return true;
  }
}
