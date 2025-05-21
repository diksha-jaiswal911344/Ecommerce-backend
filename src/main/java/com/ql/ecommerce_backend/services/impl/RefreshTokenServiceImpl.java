package com.ql.ecommerce_backend.services.impl;

import com.ql.ecommerce_backend.dto.request.TokenRefreshRequest;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.entity.RefreshToken;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.exceptions.TokenRefreshException;
import com.ql.ecommerce_backend.repository.RefreshTokenRepository;
import com.ql.ecommerce_backend.repository.UserRepository;
import com.ql.ecommerce_backend.security.JwtTokenProvider;
import com.ql.ecommerce_backend.services.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-token-validity-ms}")
    private long refreshTokenDurationMs;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
//        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    @Override
//    @Transactional
//    public RefreshToken createRefreshToken(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new TokenRefreshException("User not found with id: " + userId));
//
//        // Delete any existing refresh tokens for this user
//        refreshTokenRepository.deleteByUserId(userId);
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .user(user)
//                .token(UUID.randomUUID().toString())
//                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
//                .build();
//
//        return refreshTokenRepository.save(refreshToken);
//    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please sign in again");
        }

        // Check if token has been marked as used
        if (token.isUsed()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token has already been used. Please sign in again");
        }

        return token;
    }

//    @Override
//    public RefreshToken findByToken(String token) {
//        return refreshTokenRepository.findByToken(token)
//                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
//    }

    @Override
    @Transactional
    public ApiResponse<Object> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    String newAccessToken = jwtTokenProvider.generateTokenForUser(user.getEmail(),user.getId(),user.getRole());

                    Map<String, Object> tokens = new HashMap<>();
                    tokens.put("accessToken", newAccessToken);
                    tokens.put("refreshToken", requestRefreshToken);
                    tokens.put("userId", user.getId());
                    tokens.put("email", user.getEmail());
                    tokens.put("name", user.getName());

                    return ApiResponse.success(
                            HttpStatus.OK.value(),
                            true,
                            "Token refreshed successfully",
                            tokens
                    );
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
    }

//    @Override
//    @Transactional
//    public void deleteByUserId(Long userId) {
//        refreshTokenRepository.deleteByUserId(userId);
//    }

//    @Override
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
//    public void deleteAllExpiredTokens() {
//        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
//        log.info("Expired refresh tokens cleared");
//    }

    @Override
    @Transactional
    public ApiResponse<Void> logout(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ApiResponse.<Void>success(
                            HttpStatus.OK.value(),
                            true,
                            "Logged out successfully",
                            null
                    );
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
    }
}