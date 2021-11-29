package com.example.seek.facade;

import com.example.seek.exception.InsufficientDataException;
import com.example.seek.model.HourlyCount;
import com.example.seek.service.TrafficLightCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TrafficLightFacade {
    private final TrafficLightCounterService trafficLightCounterService;

    public void run() {
        System.out.println("\nNumber of cars passed the traffic counter: " + trafficLightCounterService.getTotalCar());


        Map<LocalDate, Integer> carCountByDay = trafficLightCounterService.getCarCountByDay();
        System.out.println("\nNumber of passed cars by day");
        for (Map.Entry<LocalDate, Integer> carCountByDayEntry : carCountByDay.entrySet()) {
            System.out.println(carCountByDayEntry.getKey() + " " + carCountByDayEntry.getValue());
        }

        System.out.println("\nTop 3 half hours with most cars");
        List<HourlyCount> listOfCars = trafficLightCounterService.getBusiestTimes();

        if (listOfCars.size() > 3) {
            for (int i = 0; i < 3; i++) {
                System.out.println(formatLocalDateTime(listOfCars.get(i).getTime()) + " " + listOfCars.get(i).getCarsCount());
            }
        }

        Map<LocalDateTime, Integer> threeQuietHalfHour;
        try {
            threeQuietHalfHour = trafficLightCounterService.find3QuietContiguousHalfHour();
            System.out.println("\nThe 1.5 hour period with least cars");
            for (Map.Entry<LocalDateTime, Integer> entry : threeQuietHalfHour.entrySet()) {
                System.out.println(formatLocalDateTime(entry.getKey()) + " " + entry.getValue());
            }
        } catch (InsufficientDataException e) {
            System.out.println("\nNo 3 contiguous half hour available");
        }
    }

    private String formatLocalDateTime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
    }

}
