package com.ql.ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ApiResponse<T> {
    private int code;
    private boolean success;
    private String message;
    @Builder.Default
    private Object data = Collections.emptyMap();

    @JsonIgnore
    private int httpStatusCode;

    public static <T> ApiResponse<T> success(int code, boolean success, String message, Object data) {
        return ApiResponse.<T>builder()
                .code(code)
                .success(success)
                .message(message)
                .data(data)
                .httpStatusCode(success ? 200 : 400)
                .build();
    }
}
