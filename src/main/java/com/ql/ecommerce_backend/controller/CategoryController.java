package com.ql.ecommerce_backend.controller;

import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.dto.response.CategoryResponse;
import com.ql.ecommerce_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        ApiResponse<List<CategoryResponse>> response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        ApiResponse<CategoryResponse> response = categoryService.getCategoryById(id);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryByName(@PathVariable String name) {
        ApiResponse<CategoryResponse> response = categoryService.getCategoryByName(name);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }
}