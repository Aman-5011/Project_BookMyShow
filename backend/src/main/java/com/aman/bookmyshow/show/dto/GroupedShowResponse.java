package com.aman.bookmyshow.show.dto;

import com.aman.bookmyshow.movie.dto.MovieCardResponse;
import com.aman.bookmyshow.movie.entity.Movie;
import com.aman.bookmyshow.theater.entity.Theater;

public record GroupedShowResponse(
        MovieCardResponse movie,
        TheaterShowResponse theater
) {}

