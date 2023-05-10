package com.example.tollcalculator.domain;

public class ForeignServiceVehicle extends Vehicle{
  @Override
  public boolean isTollFree() {
    return true;
  }
}
