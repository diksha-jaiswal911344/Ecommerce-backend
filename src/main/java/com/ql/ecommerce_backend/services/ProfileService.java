package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.dto.request.ProfileUpdateRequest;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.ProfileResponse;

public interface ProfileService {
    ApiResponse<ProfileResponse> getProfile();
    ApiResponse<ProfileResponse> updateProfile(ProfileUpdateRequest request);
}
