package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.UserOtp;
import com.ql.ecommerce_backend.enums.OtpPurpose;
import com.ql.ecommerce_backend.exceptions.InvalidOtpException;
import com.ql.ecommerce_backend.exceptions.OtpExpiredException;
import com.ql.ecommerce_backend.repository.UserOtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class OtpService {

    private final UserOtpRepository userOtpRepository;
    private final EmailService emailService;

    public OtpService(UserOtpRepository userOtpRepository, EmailService emailService) {
        this.userOtpRepository = userOtpRepository;
        this.emailService = emailService;
    }

    public void generateAndSendOtp(User user, OtpPurpose purpose) {
        String otp = generateOtp();
        saveOtp(user, otp, purpose);
        emailService.sendOtpEmail(user.getEmail(), otp, purpose);
    }

    private String generateOtp() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void saveOtp(User user, String otp, OtpPurpose purpose) {
        // Invalidate any existing OTPs for this user and purpose
        userOtpRepository.findByUserAndOtpPurposeAndIsVerifiedFalse(user, purpose)
                .forEach(existingOtp -> {
                    existingOtp.setIsVerified(true); // Mark as used
                    userOtpRepository.save(existingOtp);
                });

        // Create new OTP
        UserOtp userOtp = UserOtp.builder()
                .user(user)
                .otpCode(otp)
                .otpPurpose(purpose)
                .expiryTime(LocalDateTime.now().plusMinutes(10)) // OTP valid for 10 minutes
                .isVerified(false)
                .build();

        userOtpRepository.save(userOtp);
    }

    public void verifyOtp(User user, String otpCode, OtpPurpose purpose) {
        UserOtp userOtp = userOtpRepository.findByUserAndOtpCodeAndOtpPurposeAndIsVerified(
                        user, otpCode, purpose, false)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP"));

        if (userOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired");
        }

        userOtp.setIsVerified(true);
        userOtpRepository.save(userOtp);
    }

}