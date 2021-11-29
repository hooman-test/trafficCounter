package com.example.seek.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class HourlyCount {
    private LocalDateTime time;
    private Integer carsCount;
}
