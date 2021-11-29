package com.example.seek.fixture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class TrafficCounterDataFixture {
    public static Map<LocalDateTime, Integer> getTrafficData(int numberOfDays, int carsPerDay) {
        Map<LocalDateTime, Integer> testDataMap = new HashMap<>();
        for (int i = 0; i < numberOfDays; i++) {
            testDataMap.put(LocalDateTime.now().minus(i, ChronoUnit.DAYS), carsPerDay);
        }

        return testDataMap;
    }

    public static Map<LocalDateTime, Integer> getRandomTrafficData(int numberOfDays) {
        Map<LocalDateTime, Integer> testDataMap = new HashMap<>();
        for (int i = 0; i < numberOfDays; i++) {
            testDataMap.put(LocalDateTime.now().minus(i, ChronoUnit.DAYS), Double.valueOf(Math.random() * 100).intValue());
        }

        return testDataMap;
    }


}
