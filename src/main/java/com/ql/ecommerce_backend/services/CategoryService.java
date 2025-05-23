package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse<List<CategoryResponse>> getAllCategories();
    ApiResponse<CategoryResponse> getCategoryById(Long id);
    ApiResponse<CategoryResponse> getCategoryByName(String name);
}
