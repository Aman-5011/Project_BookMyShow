package com.aman.bookmyshow.show.dto;

import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.show.entity.SeatRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ShowDetailsResponse(
        Long id,
        MovieCardResponse movie,
        TheaterDetailsResponse theater,
        String format,
        String audioType,
        String startTime,
        LocalDate showDate,
        List<SeatRow> seatLayout
) {}
