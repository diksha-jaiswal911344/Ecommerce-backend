package com.ql.ecommerce_backend.dto.request;

import com.ql.ecommerce_backend.enums.OtpPurpose;
import com.ql.ecommerce_backend.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email should be valid")
    private String email;

    @NotNull(message = "purpose is required")
    private OtpPurpose purpose;

    @NotNull(message = "roleType required")
    private RoleType roleType;

}
