package com.aman.bookmyshow.show.dto;

import java.util.List;

public record TheaterShowResponse(
        TheaterDetailsResponse theaterDetails,
        List<ShowTimeResponse> shows
) {}
