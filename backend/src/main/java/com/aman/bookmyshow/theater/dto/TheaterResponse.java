package com.aman.bookmyshow.theater.dto;

public record TheaterResponse(
        Long id,
        String name,
        String logo,
        String address,
        String city,
        String state
) {}
