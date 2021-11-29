package com.example.seek.service;

import com.example.seek.model.HourlyCount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TrafficLightCounterService {
    int getTotalCar();

    Map<LocalDate, Integer> getCarCountByDay();

    List<HourlyCount> getBusiestTimes();

    Map<LocalDateTime, Integer> find3QuietContiguousHalfHour();
}
