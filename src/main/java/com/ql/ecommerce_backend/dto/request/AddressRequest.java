package com.ql.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private Long id; // Null for create, non-null for update

    @NotBlank(message = "address line1 is required")
    private String addressLine1;
    @NotBlank(message = "address line2 is required")
    private String addressLine2;
    @NotBlank(message = "city name is required")
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "City must contain only letters and spaces")
    private String city;
    @NotBlank(message = "state name is required")
    private String state;
    @NotBlank(message = "country name is required")
    private String country;
    @NotBlank(message = "zipcode is required")
    @Pattern(regexp = "^\\d{6}$", message = "zip code must be 6 digits")
    private String zipCode;
    @NotBlank(message = "address type is required")
    private String addressType; // HOME, WORK, etc.
    @NotNull(message = "isDefault field is required")
    private Boolean isDefault;

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1 != null? addressLine1.trim():null;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2 != null? addressLine2.trim():null;
    }

    public void setCity(String city) {
        this.city = city != null? city.trim():null;
    }

    public void setState(String state) {
        this.state = state != null? state.trim():null;
    }

    public void setCountry(String country) {
        this.country = country != null? country.trim():null;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode != null? zipCode.trim():null;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType != null?zipCode.trim():null;
    }
}
