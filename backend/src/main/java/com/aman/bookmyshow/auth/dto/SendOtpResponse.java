package com.aman.bookmyshow.auth.dto;

public record SendOtpResponse(
        String email,
        String hash
) {
}
