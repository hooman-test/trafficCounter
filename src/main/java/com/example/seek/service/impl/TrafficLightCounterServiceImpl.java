package com.example.seek.service.impl;

import com.example.seek.exception.InsufficientDataException;
import com.example.seek.model.HourlyCount;
import com.example.seek.repository.CarDataRepository;
import com.example.seek.service.TrafficLightCounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrafficLightCounterServiceImpl implements TrafficLightCounterService {
    private final CarDataRepository carDataRepository;

    @Override
    public int getTotalCar() {
        return carDataRepository.getCarData().values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public Map<LocalDate, Integer> getCarCountByDay() {
        return carDataRepository.getCarData().entrySet().stream()
                .collect(Collectors.groupingBy(i -> i.getKey().toLocalDate(), Collectors.summingInt(Map.Entry::getValue)));
    }

    @Override
    public List<HourlyCount> getBusiestTimes() {
        return carDataRepository.getCarData().entrySet().stream()
                .map(i -> HourlyCount.builder().time(i.getKey()).carsCount(i.getValue()).build())
                .sorted(Comparator.comparing(HourlyCount::getCarsCount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * This method tries to find 3 contiguous half hours with the fewest cars in the road.
     * It doesn't check for the exact contiguous half hours as the input file should be generated by another system,
     * and that is outside the scope of this application.
     * @return the map of times and the number of cars passed
     */
    @Override
    public Map<LocalDateTime, Integer> find3QuietContiguousHalfHour() {
        List<Map<LocalDateTime, Integer>> carMapList = new ArrayList<>();
        List<Map.Entry<LocalDateTime, Integer>> carMapListSorted = carDataRepository.getCarData().entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());

        for (int i = 0; i < carMapListSorted.size() - 2; i++) {
            Map<LocalDateTime, Integer> m = new TreeMap<>();
            m.put(carMapListSorted.get(i).getKey(), carMapListSorted.get(i).getValue());
            m.put(carMapListSorted.get(i + 1).getKey(), carMapListSorted.get(i + 1).getValue());
            m.put(carMapListSorted.get(i + 2).getKey(), carMapListSorted.get(i + 2).getValue());
            carMapList.add(m);
        }

        Optional<Map<LocalDateTime, Integer>> optionalItem = carMapList.stream().min(Comparator.comparingInt(o -> o.values().stream().mapToInt(Integer::intValue).sum()));
        return optionalItem.orElseThrow(() -> new InsufficientDataException("Not enough data exist to evaluate 3 contiguous half hour"));
    }

}
