package com.ql.ecommerce_backend.dto.request;

import com.ql.ecommerce_backend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;
}
