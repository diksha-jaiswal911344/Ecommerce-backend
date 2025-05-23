package com.ql.ecommerce_backend.controller;

import com.ql.ecommerce_backend.dto.request.ProfileUpdateRequest;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.ProfileResponse;
import com.ql.ecommerce_backend.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
        ApiResponse<ProfileResponse> response = profileService.getProfile();
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(@RequestBody ProfileUpdateRequest request) {
        ApiResponse<ProfileResponse> response = profileService.updateProfile(request);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}
