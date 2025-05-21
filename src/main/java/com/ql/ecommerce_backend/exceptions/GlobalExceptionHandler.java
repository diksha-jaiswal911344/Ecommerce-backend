 package com.ql.ecommerce_backend.exceptions;
//
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(BaseException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.error("Error ID: {} - {}: {}", errorId, ex.getClass().getSimpleName(), ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(ex.getErrorCode())
                .success(false)
                .message(ex.getMessage())
                .httpStatusCode(ex.getHttpStatus())
                .build();

        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.error("Error ID: {} - Validation error", errorId, ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> response = ApiResponse.builder()
                .code(400)
                .success(false)
                .message("Validation Failed")
                .data(errors)
                .httpStatusCode(400)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleOtpExpiredException(
            OtpExpiredException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.warn("Error ID: {} - OTP expired: {}", errorId, ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(400)
                .success(false)
                .message("OTP expired")
                .data(Map.of("error", ex.getMessage()))
                .httpStatusCode(400)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.error("Error ID: {} - Authentication failed: {}", errorId, ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(403)
                .success(false)
                .message(ex.getMessage())
                .data(Map.of("error", ex.getMessage())) // optional: add errorId or other info here
                .httpStatusCode(403)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.error("Error ID: {} - Entity not found: {}", errorId, ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(404)
                .success(false)
                .message(ex.getMessage())
                .httpStatusCode(404)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.error("Error ID: {} - Constraint violation: {}", errorId, ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });

        ApiResponse<Object> response = ApiResponse.builder()
                .code(400)
                .success(false)
                .message("Validation Failed")
                .data(errors)
                .httpStatusCode(400)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {

        String errorId = generateErrorId();
        logger.error("Error ID: {} - Data integrity violation: {}", errorId, ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(409)
                .success(false)
                .message("Data integrity violation. Possible duplicate entry or invalid data.")
                .httpStatusCode(409)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.error("Error ID: {} - Unhandled exception: {}", errorId, ex.getMessage(), ex);

        ApiResponse<Object> response = ApiResponse.builder()
                .code(500)
                .success(false)
                .message("An unexpected error occurred. Reference: " + errorId)
                .httpStatusCode(500)
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //When a required query parameter or form parameter is missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMissingRequestParamException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("parameter", ex.getParameterName()); // which parameter was missing
        errorDetails.put("path", request.getRequestURI());    // API path
        errorDetails.put("timestamp", Instant.now().toString());
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.success(HttpStatus.BAD_REQUEST.value(), false, "Missing required request parameter: " + ex.getParameterName(), errorDetails));
    }

    //when a required path variable is missing in a controller method
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getRequestURI());                            // The endpoint hit
        errorDetails.put("timestamp", Instant.now().toString());                      // Current time
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());         // "Bad Request"

        // Add the name of the missing variable
        errorDetails.put("missingPathVariable", ex.getVariableName());

        // Return structured error response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.success(HttpStatus.BAD_REQUEST.value(), false, "Missing path variable: " + ex.getVariableName(), errorDetails));
    }

    //When the request body is missing or contains malformed/invalid JSON that cannot be deserialized
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", Instant.now().toString());
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        // Return structured ApiResponseNew
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.success(HttpStatus.BAD_REQUEST.value(), false, "Invalid or malformed JSON request body", errorDetails));
    }

    //Request parameter or path variable can't be converted to the required data type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getRequestURI());                                      // URI that failed
        errorDetails.put("timestamp", Instant.now().toString());                                // When it failed
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());                   // "Bad Request"
        errorDetails.put("parameterName", ex.getName());                                        // e.g. "id"
        errorDetails.put("invalidValue", String.valueOf(ex.getValue()));                        // e.g. "abc"
        errorDetails.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.success(HttpStatus.BAD_REQUEST.value(), false, "Invalid type for parameter: " + ex.getName(), errorDetails));
    }

    //Request using an HTTP method (GET, POST, PUT, DELETE, etc.) that is not supported by the endpoint
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", Instant.now().toString());
        errorDetails.put("error", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        errorDetails.put("unsupportedMethod", ex.getMethod());
        errorDetails.put("supportedMethods", String.join(", ", ex.getSupportedMethods()));

        // Return response in consistent format
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponse.success(HttpStatus.METHOD_NOT_ALLOWED.value(), false, "HTTP method not supported for this endpoint", errorDetails));
    }

    //Content-Type (e.g., application/json, multipart/form-data, etc.) of the request is not supported by the API endpoint
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", Instant.now().toString());
        errorDetails.put("error", HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());        // Unsupported Media Type
        errorDetails.put("unsupportedMediaType", ex.getContentType() != null ? ex.getContentType().toString() : "Unknown");
        errorDetails.put("supportedMediaTypes", ex.getSupportedMediaTypes().toString());       // Allowed types
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ApiResponse.success(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), false, "Content-Type not supported", errorDetails));
    }

    /**
     * Generate a unique error ID for tracing
     */
    private String generateErrorId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}