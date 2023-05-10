package com.example.tollcalculator.service;

import com.example.tollcalculator.domain.Vehicle;
import java.time.LocalDateTime;
import java.util.List;

public interface TollService {
  double calculateTollForPasses(Vehicle vehicle, List<LocalDateTime> passTimestamps);
}
