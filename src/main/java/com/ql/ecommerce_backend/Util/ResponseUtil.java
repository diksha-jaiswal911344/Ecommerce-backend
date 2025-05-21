package com.ql.ecommerce_backend.Util;

import com.ql.ecommerce_backend.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

/**
 * Utility class to create standardized API responses
 */
public class ResponseUtil {

    /**
     * Creates a success response with data
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, Object data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(200)
                .success(true)
                .message(message)
                .data(data)
                .httpStatusCode(200)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Creates a success response without data
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return success(message, Collections.emptyMap());
    }

    /**
     * Creates a created response (201) with data
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, Object data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(201)
                .success(true)
                .message(message)
                .data(data)
                .httpStatusCode(201)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Creates a created response (201) without data
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
        return created(message, Collections.emptyMap());
    }

    /**
     * Creates a no content response (204)
     */
    public static <T> ResponseEntity<ApiResponse<T>> noContent() {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(204)
                .success(true)
                .message("No Content")
                .data(Collections.emptyMap())
                .httpStatusCode(204)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Creates a bad request response (400) with error message
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(400)
                .success(false)
                .message(message)
                .httpStatusCode(400)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a bad request response (400) with error data
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, Object errorData) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(400)
                .success(false)
                .message(message)
                .data(errorData)
                .httpStatusCode(400)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates an unauthorized response (401)
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(401)
                .success(false)
                .message(message)
                .httpStatusCode(401)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Creates a forbidden response (403)
     */
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(403)
                .success(false)
                .message(message)
                .httpStatusCode(403)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Creates a not found response (404)
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(404)
                .success(false)
                .message(message)
                .httpStatusCode(404)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a custom response with specific HTTP status
     */
    public static <T> ResponseEntity<ApiResponse<T>> custom(int code, boolean success, String message, Object data, HttpStatus httpStatus) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .code(code)
                .success(success)
                .message(message)
                .data(data)
                .httpStatusCode(httpStatus.value())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }
}
