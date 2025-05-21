package com.ql.ecommerce_backend.dto.request;


import com.ql.ecommerce_backend.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ql.ecommerce_backend.enums.OtpPurpose.PASSWORD_RESET;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

//    private OtpPurpose purpose= PASSWORD_RESET;
}
