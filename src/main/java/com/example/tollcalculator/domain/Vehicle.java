package com.example.tollcalculator.domain;

/**
 * The vehicle abstract class.
 */
public abstract class Vehicle {
  /**
   * Concrete vehicles provide implementation.
   *
   * @return true if the Concrete vehicle is toll-free, false otherwise.
   */
  public abstract boolean isTollFree();
}
