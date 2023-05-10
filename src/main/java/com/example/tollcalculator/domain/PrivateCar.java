package com.example.tollcalculator.domain;

public class PrivateCar extends Vehicle {
  @Override
  public boolean isTollFree() {
    return false;
  }
}
