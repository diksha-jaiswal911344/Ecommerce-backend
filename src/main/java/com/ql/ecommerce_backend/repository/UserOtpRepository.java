package com.ql.ecommerce_backend.repository;

import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.UserOtp;
import com.ql.ecommerce_backend.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
//    Optional<UserOtp> findByUserAndOtpCodeAndExpiryTimeAfterAndIsVerifiedFalse(
//            User user, String otpCode, LocalDateTime now);
    List<UserOtp> findByUserAndOtpPurposeAndIsVerifiedFalse(User user, OtpPurpose otpPurpose);

    Optional<UserOtp> findTopByUserAndOtpPurposeOrderByCreatedAtDesc(User user, OtpPurpose otpPurpose);

    Optional<UserOtp> findByUserAndOtpCodeAndOtpPurposeAndIsVerified(User user, String otpCode, OtpPurpose otpPurpose, boolean isVerified);
}