package com.ql.ecommerce_backend.dto.mapper;

import com.ql.ecommerce_backend.dto.response.AddressResponse;
import com.ql.ecommerce_backend.dto.response.CategoryResponse;
import com.ql.ecommerce_backend.entity.Category;
import com.ql.ecommerce_backend.entity.UserAddress;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryResponse toDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryResponse> toDto(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

}
