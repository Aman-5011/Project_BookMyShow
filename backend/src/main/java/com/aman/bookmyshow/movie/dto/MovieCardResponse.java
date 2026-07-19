package com.aman.bookmyshow.movie.dto;

import java.util.List;

public record MovieCardResponse(
    Long id ,
    String title ,
    String posterUrl,
    Double rating,
    Integer votes,
    String certification,
    List<String> language
) { }
