package com.ql.ecommerce_backend.dto.request;

import com.ql.ecommerce_backend.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "email is required")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "name required")
    private String name;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String createPassword;

    @NotBlank(message = "confirm password is required")
    private String confirmPassword;

    @NotNull(message = "roleType required")
    private RoleType roleType;
}
