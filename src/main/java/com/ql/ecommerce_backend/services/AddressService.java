package com.ql.ecommerce_backend.services;

import com.ql.ecommerce_backend.dto.request.AddressRequest;
import com.ql.ecommerce_backend.dto.response.AddressResponse;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    ApiResponse<List<AddressResponse>> getAllAddresses();
    ApiResponse<AddressResponse> getAddressById(Long id);
    ApiResponse<AddressResponse> createAddress(AddressRequest request);
    ApiResponse<AddressResponse> updateAddress(Long id, AddressRequest request);
    ApiResponse<String> deleteAddress(Long id);
    ApiResponse<AddressResponse> setDefaultAddress(Long id);
}
