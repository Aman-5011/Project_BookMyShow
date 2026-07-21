package com.aman.bookmyshow.user.dto;

public record UserResponse(
        Long id,
        String email,
        String name,
        String phone,
        Boolean activateUser
) {}
