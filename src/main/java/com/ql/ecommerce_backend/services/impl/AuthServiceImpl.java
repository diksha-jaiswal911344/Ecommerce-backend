package com.ql.ecommerce_backend.services.impl;

import com.ql.ecommerce_backend.dto.mapper.UserMapper;
import com.ql.ecommerce_backend.dto.request.*;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.RefreshToken;
import com.ql.ecommerce_backend.enums.AccountStatus;
import com.ql.ecommerce_backend.exceptions.AuthenticationException;
import com.ql.ecommerce_backend.exceptions.InvalidRequestException;
import com.ql.ecommerce_backend.exceptions.ResourceNotFoundException;
import com.ql.ecommerce_backend.exceptions.TokenRefreshException;
import com.ql.ecommerce_backend.repository.UserRepository;
import com.ql.ecommerce_backend.repository.RefreshTokenRepository;
import com.ql.ecommerce_backend.security.JwtTokenProvider;
import com.ql.ecommerce_backend.services.AuthService;
import com.ql.ecommerce_backend.services.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.ql.ecommerce_backend.enums.OtpPurpose.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpService otpService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            OtpService otpService,
            RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.otpService = otpService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public ApiResponse<Map<String, Object>> signup(SignupRequest signupRequest) {
        // Validate passwords match
        if (!Objects.equals(signupRequest.getCreatePassword(), signupRequest.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new InvalidRequestException("Email already registered");
        }

        // Create new user
        User user = userMapper.toEntity(signupRequest);
        user = userRepository.save(user);

        // Generate tokens
        Map<String, Object> tokens = generateAuthTokens(user);

        return ApiResponse.success(
                HttpStatus.CREATED.value(),
                true,
                "User registered successfully",
                tokens
        );
    }

    @Override
    public ApiResponse<Void> requestSignupOtp(OtpRequest otpRequest) {
        // Check if user already exists
        userRepository.findByEmail(otpRequest.getEmail()).ifPresent(user -> {
            throw new InvalidRequestException("Email already registered");
        });

        // Create a temporary user to associate with OTP
        User tempUser = User.builder()
                .email(otpRequest.getEmail())
                .name(otpRequest.getEmail().split("@")[0])
                .accountStatus(AccountStatus.PENDING)
                .role(otpRequest.getRoleType())
                .build();

        User savedUser = userRepository.save(tempUser);

        // Generate and send OTP
        otpService.generateAndSendOtp(savedUser, SIGNUP);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "OTP sent successfully",
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<Map<String, Object>> verifySignupOtp(OtpVerificationRequest otpVerificationRequest) {
        log.info("Verifying OTP for email: {}, otp: {}, purpose: {}",
                otpVerificationRequest.getEmail(),
                otpVerificationRequest.getOtp(),
                otpVerificationRequest.getPurpose());

        User user = userRepository.findByEmail(otpVerificationRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify OTP
        otpService.verifyOtp(user, otpVerificationRequest.getOtp(), SIGNUP);

        // Generate tokens
        Map<String, Object> tokens = generateAuthTokens(user);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "OTP verified successfully",
                tokens
        );
    }

    @Transactional
    @Override
    public ApiResponse<Map<String, Object>> signin(SigninRequest signinRequest) {
        try {
            // Get user details
            User user = userRepository.findByEmail(signinRequest.getEmail())
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            log.info("Stored hash: {}", user.getHashPassword());
            log.info("Incoming raw password: {}", signinRequest.getPassword());

            if (!passwordEncoder.matches(signinRequest.getPassword(), user.getHashPassword())) {
                throw new AuthenticationException("Invalid email or password");
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Generate tokens
            Map<String, Object> tokens = generateAuthTokens(user);

            return ApiResponse.success(
                    HttpStatus.OK.value(),
                    true,
                    "Login successful",
                    tokens
            );
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new AuthenticationException("Invalid credentials");
        }
    }

    @Override
    public ApiResponse<Void> requestSigninOtp(OtpRequest otpRequest) {
        User user = userRepository.findByEmail(otpRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate and send OTP
        otpService.generateAndSendOtp(user, LOGIN);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "OTP sent successfully",
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<Map<String, Object>> verifySigninOtp(OtpVerificationRequest otpVerificationRequest) {
        User user = userRepository.findByEmail(otpVerificationRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify OTP
        otpService.verifyOtp(user, otpVerificationRequest.getOtp(), LOGIN);

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        Map<String, Object> tokens = generateAuthTokens(user);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "OTP verified successfully",
                tokens
        );
    }

    @Override
    public ApiResponse<Void> requestPasswordReset(ForgetPasswordRequest forgetPasswordRequest) {
        User user = userRepository.findByEmail(forgetPasswordRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate and send OTP
        otpService.generateAndSendOtp(user, PASSWORD_RESET);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "Password reset OTP sent successfully",
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<Void> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        // Validate passwords match
        if (!Objects.equals(updatePasswordRequest.getNewPassword(), updatePasswordRequest.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }

        User user = userRepository.findByEmail(updatePasswordRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify OTP
        otpService.verifyOtp(user, updatePasswordRequest.getOtp(), PASSWORD_RESET);

        // Update password
        user.setHashPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "Password updated successfully",
                null
        );
    }

    @Override
    @Transactional
    public ApiResponse<Map<String, Object>> completeUserOnboarding(OnboardingRequest onboardingRequest, String token) {
        // Get user from token
        User user = getUserFromToken(token);

        // Update user with onboarding details
        userMapper.updateUserWithOnboardingDetails(user, onboardingRequest);
        userRepository.save(user);

        // Return user ID in response
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());

        return ApiResponse.success(
                HttpStatus.OK.value(),
                true,
                "User onboarding completed successfully",
                response
        );
    }

    @Override
    public User getUserFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    //Generates auth tokens (access token and refresh token) for a user
    private Map<String, Object> generateAuthTokens(User user) {
        String accessToken = jwtTokenProvider.generateTokenForUser(user.getEmail(),user.getId(),user.getRole());

        // Create refresh token
        RefreshToken refreshToken = createRefreshToken(user);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken.getToken());
        tokens.put("userId", user.getId());
        tokens.put("email", user.getEmail());
        tokens.put("name", user.getName());

        return tokens;
    }

    //Creates a new refresh token for a user
    private RefreshToken createRefreshToken(User user) {
        // Delete any existing refresh tokens for this user
        refreshTokenRepository.deleteByUserId(user.getId());

        // Create a new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenValidity()));

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    //Refresh access token using refresh token
    public ApiResponse< Object> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
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

    //Verify if the refresh token is expired
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please sign in again");
        }

        return token;
    }
}