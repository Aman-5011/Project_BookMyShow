package com.aman.bookmyshow.auth.service;

import com.aman.bookmyshow.auth.dto.SendOtpRequest;
import com.aman.bookmyshow.auth.dto.SendOtpResponse;
import com.aman.bookmyshow.auth.dto.VerifyOtpRequest;
import com.aman.bookmyshow.auth.dto.VerifyOtpResponse;
import com.aman.bookmyshow.common.util.HashUtil;
import com.aman.bookmyshow.otp.entity.Otp;
import com.aman.bookmyshow.otp.repo.OtpRepo;
import com.aman.bookmyshow.user.dto.UserResponse;
import com.aman.bookmyshow.user.entity.User;
import com.aman.bookmyshow.user.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;

    public AuthService(UserRepo userRepo, OtpRepo otpRepo) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
    }

    public SendOtpResponse sendOtp(SendOtpRequest request) {

        String email = request.email();

        SecureRandom random = new SecureRandom();
        int otp = 1000 + random.nextInt(9000);

        System.out.println("Generated OTP : " + otp);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);

        Otp otpEntity = otpRepo.findByEmail(email)
                .orElseGet(() -> {
                    Otp newOtp = new Otp();
                    newOtp.setEmail(email);
                    return newOtp;
                });

        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);

        otpEntity = otpRepo.save(otpEntity);

        long expiryMillis = otpEntity.getExpiryTime()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        String hashData =
                otpEntity.getEmail()
                        + otpEntity.getOtp()
                        + expiryMillis;

        String hash = HashUtil.generateHash(hashData);

        return new SendOtpResponse(
                otpEntity.getEmail(),
                hash
        );
    }

    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

        String email = request.email();
        Integer otp = request.otp();
        String receivedHash = request.hash();

        Otp savedOtp = otpRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (savedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!savedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        long expiryMillis = savedOtp.getExpiryTime()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        String hashData =
                savedOtp.getEmail()
                        + savedOtp.getOtp()
                        + expiryMillis;

        String expectedHash = HashUtil.generateHash(hashData);

        if (!expectedHash.equals(receivedHash)) {
            throw new RuntimeException("Hash mismatch");
        }

        User user = userRepo.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setActivateUser(false);
                    return userRepo.save(newUser);
                });

        otpRepo.delete(savedOtp);

        return new VerifyOtpResponse(
                new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getPhone(),
                        user.getActivateUser()
                )
        );
    }
}