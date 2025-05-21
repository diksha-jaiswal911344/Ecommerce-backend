package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.dto.request.TokenRefreshRequest;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.entity.RefreshToken;
import java.util.Map;
public interface RefreshTokenService {

//    /**
//     * Create a new refresh token for a user
//     */
//    RefreshToken createRefreshToken(Long userId);

    /**
     * Verify if a refresh token is valid and not expired
     */
    RefreshToken verifyExpiration(RefreshToken token);
//
//    /**
//     * Find a refresh token by its token value
//     */
//    RefreshToken findByToken(String token);

    /**
     * Process a token refresh request and generate a new access token
     */
    ApiResponse< Object> refreshToken(TokenRefreshRequest request);
//
//    /**
//     * Delete all refresh tokens for a user
//     */
//    void deleteByUserId(Long userId);

//    /**
//     * Delete all expired tokens
//     */
//    void deleteAllExpiredTokens();

    /**
     * Logout user by invalidating their refresh tokens
     */
    ApiResponse<Void> logout(String refreshToken);
}