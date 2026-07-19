package com.aman.bookmyshow.movie.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieDetailsResponse(
        Long id,
        String title,
        String description,
        String posterUrl,
        Double rating,
        Integer votes,
        List<String> languages,
        List<String> genre,
        Integer durationMinutes,
        List<String> format,
        LocalDate releaseDate,
        String certification
) {}
