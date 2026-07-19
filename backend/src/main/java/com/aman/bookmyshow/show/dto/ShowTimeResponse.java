package com.aman.bookmyshow.show.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShowTimeResponse(
        Long id,
        String startTime,
        String format,
        String audioType,
        LocalDate showDate
) {}
