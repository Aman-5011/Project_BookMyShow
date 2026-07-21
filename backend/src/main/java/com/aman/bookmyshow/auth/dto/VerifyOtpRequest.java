package com.aman.bookmyshow.auth.dto;

public record VerifyOtpRequest(
        String email,
        Integer otp,
        String hash
) {}
