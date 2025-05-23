package com.ql.ecommerce_backend.dto.mapper;

import com.ql.ecommerce_backend.dto.request.OnboardingRequest;
import com.ql.ecommerce_backend.dto.request.SignupRequest;
import com.ql.ecommerce_backend.dto.response.ProfileResponse;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.enums.AccountStatus;
import com.ql.ecommerce_backend.enums.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public ProfileResponse toProfileResponse(User user) {
        ProfileResponse response = new ProfileResponse();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setAge(user.getAge());
        response.setGender(user.getGender());
        return response;
    }
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(SignupRequest signupRequest) {
        return User.builder()
                .email(signupRequest.getEmail())
                .hashPassword(passwordEncoder.encode(signupRequest.getCreatePassword()))
                .role(RoleType.ROLE_BUYER) // Default role for signup
                .accountStatus(AccountStatus.PENDING)
                .name(signupRequest.getName()) // Temporary name until onboarding
                .build();
    }

    public void updateUserWithOnboardingDetails(User user, OnboardingRequest onboardingRequest) {
        user.setFirstName(onboardingRequest.getFirstName());
        user.setLastName(onboardingRequest.getLastName());
        user.setGender(onboardingRequest.getGender());
        user.setAge(onboardingRequest.getAge());
        user.setName(onboardingRequest.getFirstName() + " " + onboardingRequest.getLastName());
        user.setAccountStatus(AccountStatus.ACTIVE);
    }
}

