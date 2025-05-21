package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.dto.request.*;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.entity.User;
import java.util.Map;

public interface AuthService {

    ApiResponse<?> signup(SignupRequest signupRequest);

    ApiResponse<?> requestSignupOtp(OtpRequest otpRequest);

    ApiResponse<?> verifySignupOtp(OtpVerificationRequest otpVerificationRequest);

    ApiResponse<?> signin(SigninRequest signinRequest);

    ApiResponse<?> requestSigninOtp(OtpRequest otpRequest);

    ApiResponse<?> verifySigninOtp(OtpVerificationRequest otpVerificationRequest);

    ApiResponse<?> requestPasswordReset(ForgetPasswordRequest forgetPasswordRequest);

    ApiResponse<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    ApiResponse<?> completeUserOnboarding(OnboardingRequest onboardingRequest, String token);

    User getUserFromToken(String token);
}
