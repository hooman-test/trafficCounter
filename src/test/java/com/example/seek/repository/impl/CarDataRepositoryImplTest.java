package com.example.seek.repository.impl;

import com.example.seek.exception.EntityNotFoundException;
import com.example.seek.utility.FileParser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarDataRepositoryImplTest {

    @InjectMocks
    private CarDataRepositoryImpl classUnderTest;

    @Mock
    private FileParser fileParser;

    @SneakyThrows
    @Test
    void getCarDataForNullFileName() {
        when(fileParser.parseTrafficCounterFile(null)).thenThrow(IOException.class);

        assertThrows(EntityNotFoundException.class, () -> classUnderTest.getCarData());
    }

    @SneakyThrows
    @Test
    void getCarDataForEmptyFile() {
        List<String> fileContent = new ArrayList<>();

        when(fileParser.parseTrafficCounterFile(anyString())).thenReturn(fileContent);

        classUnderTest.setFilePath("/path/to/file");
        Map<LocalDateTime, Integer> result = classUnderTest.getCarData();

        assertEquals(0, result.size());
    }

    @SneakyThrows
    @Test
    void getCarDataForValidFile() {
        List<String> fileContent = new ArrayList<>();
        fileContent.add("2016-12-01T15:30:00 11");

        when(fileParser.parseTrafficCounterFile(anyString())).thenReturn(fileContent);

        classUnderTest.setFilePath("/path/to/file");
        Map<LocalDateTime, Integer> result = classUnderTest.getCarData();

        assertEquals(1, result.size());
    }
}