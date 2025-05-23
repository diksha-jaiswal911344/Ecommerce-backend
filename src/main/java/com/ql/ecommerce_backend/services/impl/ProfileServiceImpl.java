package com.ql.ecommerce_backend.services.impl;

import com.ql.ecommerce_backend.dto.mapper.UserMapper;
import com.ql.ecommerce_backend.dto.request.ProfileUpdateRequest;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.ProfileResponse;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.exceptions.ResourceNotFoundException;
import com.ql.ecommerce_backend.repository.UserRepository;
import com.ql.ecommerce_backend.services.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<ProfileResponse> getProfile() {
        User currentUser = getCurrentUser();
        ProfileResponse response = userMapper.toProfileResponse(currentUser);
        return ApiResponse.success(200, true, "Profile fetched successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<ProfileResponse> updateProfile(ProfileUpdateRequest request) {
        User user = getCurrentUser();

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getGender() != null) user.setGender(request.getGender());

        User updatedUser = userRepository.save(user);
        ProfileResponse response = userMapper.toProfileResponse(updatedUser);

        return ApiResponse.success(200, true, "Profile updated successfully", response);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
