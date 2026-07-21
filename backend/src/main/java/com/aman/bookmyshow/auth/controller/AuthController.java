package com.aman.bookmyshow.auth.controller;

import com.aman.bookmyshow.auth.dto.SendOtpRequest;
import com.aman.bookmyshow.auth.dto.SendOtpResponse;
import com.aman.bookmyshow.auth.dto.VerifyOtpRequest;
import com.aman.bookmyshow.auth.dto.VerifyOtpResponse;
import com.aman.bookmyshow.auth.service.AuthService;
import com.aman.bookmyshow.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<SendOtpResponse>> sendOtp( @RequestBody SendOtpRequest request ) {
        SendOtpResponse response = authService.sendOtp(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "OTP sent successfully",
                        response
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp( @RequestBody VerifyOtpRequest request ) {
        VerifyOtpResponse response = authService.verifyOtp(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "OTP verified successfully",
                        response
                )
        );
    }
}