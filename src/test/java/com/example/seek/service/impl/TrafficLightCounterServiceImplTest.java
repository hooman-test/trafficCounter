package com.example.seek.service.impl;

import com.example.seek.exception.InsufficientDataException;
import com.example.seek.fixture.TrafficCounterDataFixture;
import com.example.seek.model.HourlyCount;
import com.example.seek.repository.CarDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrafficLightCounterServiceImplTest {

    @InjectMocks
    private TrafficLightCounterServiceImpl classUnderTest;

    @Mock
    private CarDataRepository carDataRepository;

    @Test
    void getTotalCar() {
        int numberOfDays = 3;
        int carsPerDay = 15;
        when(carDataRepository.getCarData()).thenReturn(TrafficCounterDataFixture.getTrafficData(numberOfDays, carsPerDay));

        int totalCars = classUnderTest.getTotalCar();

        assertEquals(numberOfDays * carsPerDay, totalCars);
        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void getTotalCarIfNoDataIsAvailable() {
        Map<LocalDateTime, Integer> sampleData = new HashMap<>();
        when(carDataRepository.getCarData()).thenReturn(sampleData);

        int totalCars = classUnderTest.getTotalCar();

        assertEquals(0, totalCars);
        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void getCarCountByDay() {
        int numberOfDays = 3;
        int carsPerDay = 15;
        when(carDataRepository.getCarData()).thenReturn(TrafficCounterDataFixture.getTrafficData(numberOfDays, carsPerDay));

        Map<LocalDate, Integer> carPerDayMap = classUnderTest.getCarCountByDay();

        assertEquals(numberOfDays, carPerDayMap.size());

        for (Map.Entry<LocalDate, Integer> entry : carPerDayMap.entrySet()) {
            assertEquals(carsPerDay, entry.getValue());
        }

        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void getBusiestTimes() {
        Map<LocalDateTime, Integer> trafficData = new HashMap<>();
        trafficData.put(LocalDateTime.now(), 5);
        trafficData.put(LocalDateTime.now().minus(1, ChronoUnit.DAYS), 15);
        trafficData.put(LocalDateTime.now().minus(2, ChronoUnit.DAYS), 25);

        when(carDataRepository.getCarData()).thenReturn(trafficData);

        List<HourlyCount> hourlyCountList = classUnderTest.getBusiestTimes();

        assertEquals(25, hourlyCountList.get(0).getCarsCount());
        assertNotNull(hourlyCountList.get(0).getTime());
        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void getBusiestTimesForOnly1RowOfData() {
        Map<LocalDateTime, Integer> trafficData = new HashMap<>();
        final LocalDateTime now = LocalDateTime.now();
        trafficData.put(now, 50);

        when(carDataRepository.getCarData()).thenReturn(trafficData);

        List<HourlyCount> hourlyCountList = classUnderTest.getBusiestTimes();

        assertEquals(50, hourlyCountList.get(0).getCarsCount());
        assertEquals(now, hourlyCountList.get(0).getTime());
        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void find3QuietContiguousHalfHour() {
        Map<LocalDateTime, Integer> trafficData = new HashMap<>();
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        trafficData.put(now, 5);
        final LocalDateTime last30 = now.minus(30, ChronoUnit.MINUTES);
        trafficData.put(last30, 10);
        final LocalDateTime last60 = now.minus(60, ChronoUnit.MINUTES);
        trafficData.put(last60, 15);
        trafficData.put(now.minus(90, ChronoUnit.MINUTES), 20);
        trafficData.put(now.minus(120, ChronoUnit.MINUTES), 50);
        trafficData.put(now.minus(150, ChronoUnit.MINUTES), 90);

        when(carDataRepository.getCarData()).thenReturn(trafficData);

        Map<LocalDateTime, Integer> quietTimes = classUnderTest.find3QuietContiguousHalfHour();

        assertEquals(3, quietTimes.size());
        assertEquals(1, quietTimes.keySet().stream().filter(i -> i == now).count());
        assertEquals(1, quietTimes.keySet().stream().filter(i -> i == last30).count());
        assertEquals(1, quietTimes.keySet().stream().filter(i -> i == last60).count());
        verify(carDataRepository, times(1)).getCarData();
    }

    @Test
    void find3QuietContiguousHalfHourForInsufficientData() {
        Map<LocalDateTime, Integer> trafficData = new HashMap<>();
        final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        trafficData.put(now, 5);
        final LocalDateTime last30 = now.minus(30, ChronoUnit.MINUTES);
        trafficData.put(last30, 10);


        when(carDataRepository.getCarData()).thenReturn(trafficData);

        assertThrows(InsufficientDataException.class, () -> classUnderTest.find3QuietContiguousHalfHour());

        verify(carDataRepository, times(1)).getCarData();
    }

}