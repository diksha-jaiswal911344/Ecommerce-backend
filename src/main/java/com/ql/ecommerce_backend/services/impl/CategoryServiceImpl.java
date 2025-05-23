package com.ql.ecommerce_backend.services.impl;

import com.ql.ecommerce_backend.dto.mapper.CategoryMapper;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.CategoryResponse;
import com.ql.ecommerce_backend.entity.Category;
import com.ql.ecommerce_backend.exceptions.ResourceNotFoundException;
import com.ql.ecommerce_backend.repository.CategoryRepository;
import com.ql.ecommerce_backend.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<Category> categories= categoryRepository.findAll();
        List<CategoryResponse> dtos = CategoryMapper.toDto(categories);
        return ApiResponse.success(200, true, "fetched all categories",dtos);
    }

    @Override
    public ApiResponse<CategoryResponse> getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return ApiResponse.success(200,true, "fetched category by id",CategoryMapper.toDto(category));
    }

    @Override
    public ApiResponse<CategoryResponse> getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
        return ApiResponse.success(200,true,"fetched category by name",CategoryMapper.toDto(category));
    }
}
