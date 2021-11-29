package com.example.seek.repository.impl;

import com.example.seek.exception.EntityNotFoundException;
import com.example.seek.repository.CarDataRepository;
import com.example.seek.utility.FileParser;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@ConfigurationProperties
@RequiredArgsConstructor
public class CarDataRepositoryImpl implements CarDataRepository {
    private final FileParser fileParse;
    private String filePath;
    private Map<LocalDateTime, Integer> carData;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Map<LocalDateTime, Integer> getCarData() {

        if (Objects.isNull(carData)) {

            List<String> fileContent;
            try {
                fileContent = fileParse.parseTrafficCounterFile(filePath);
            } catch (IOException e) {
                throw new EntityNotFoundException("Cannot parse file at: " + filePath);
            }

            carData = fileContent.stream().collect(
                    Collectors.groupingBy(item -> LocalDateTime.parse(item.split(" ")[0]),
                            Collectors.summingInt(i -> Integer.parseInt(i.split(" ")[1]))));
        }

        return carData;
    }
}
