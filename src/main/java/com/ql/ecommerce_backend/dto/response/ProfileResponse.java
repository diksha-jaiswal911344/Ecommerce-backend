package com.ql.ecommerce_backend.dto.response;

import com.ql.ecommerce_backend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;
}