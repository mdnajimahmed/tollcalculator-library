package com.example.tollcalculator.domain;

public class EmergencyServiceVehicle extends Vehicle{
  @Override
  public boolean isTollFree() {
    return true;
  }
}
