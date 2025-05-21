package com.ql.ecommerce_backend.controller;

import com.ql.ecommerce_backend.dto.request.*;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.services.AuthService;
import com.ql.ecommerce_backend.services.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    public AuthController(RefreshTokenService refreshTokenService, AuthService authService) {
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
    }

    //register a new user
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        ApiResponse<?> response = authService.signup(signupRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/signup/request-otp")
    public ResponseEntity<ApiResponse<?>> requestSignupOtp(@Valid @RequestBody OtpRequest otpRequest) {
        ApiResponse<?> response = authService.requestSignupOtp(otpRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifySignupOtp(@Valid @RequestBody OtpVerificationRequest verificationRequest) {
        ApiResponse<?> response = authService.verifySignupOtp(verificationRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signin(@Valid @RequestBody SigninRequest signinRequest) {
        ApiResponse<?> response = authService.signin(signinRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/signin/request-otp")
    public ResponseEntity<ApiResponse<?>> requestSigninOtp(@Valid @RequestBody OtpRequest otpRequest) {
        ApiResponse<?> response = authService.requestSigninOtp(otpRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/signin/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifySigninOtp(@Valid @RequestBody OtpVerificationRequest verificationRequest) {
        ApiResponse<?> response = authService.verifySigninOtp(verificationRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/password/request-reset")
    public ResponseEntity<ApiResponse<?>> requestPasswordReset(@Valid @RequestBody ForgetPasswordRequest passwordRequest) {
        ApiResponse<?> response = authService.requestPasswordReset(passwordRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/password/update")
    public ResponseEntity<ApiResponse<?>> updatePassword(@Valid @RequestBody UpdatePasswordRequest updateRequest) {
        ApiResponse<?> response = authService.updatePassword(updateRequest);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<?>> completeUserOnboarding(
            @Valid @RequestBody OnboardingRequest onboardingRequest,
            @RequestHeader("Authorization") String authHeader) {
        ApiResponse<?> response = authService.completeUserOnboarding(onboardingRequest, authHeader);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    //to get new access token
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        ApiResponse< Object> response = refreshTokenService.refreshToken(request);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody TokenRefreshRequest request) {
        ApiResponse<Void> response = refreshTokenService.logout(request.getRefreshToken());
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
