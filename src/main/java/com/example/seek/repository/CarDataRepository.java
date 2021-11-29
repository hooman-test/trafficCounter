package com.example.seek.repository;

import java.time.LocalDateTime;
import java.util.Map;

public interface CarDataRepository {
    Map<LocalDateTime, Integer> getCarData();
}
