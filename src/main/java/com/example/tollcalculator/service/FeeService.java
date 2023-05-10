package com.example.tollcalculator.service;

import java.time.LocalTime;

public interface FeeService {
  Double getTollRate(LocalTime pass);
}
