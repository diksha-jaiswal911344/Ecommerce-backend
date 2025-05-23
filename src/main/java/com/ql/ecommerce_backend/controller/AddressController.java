package com.ql.ecommerce_backend.controller;

import com.ql.ecommerce_backend.Util.ResponseUtil;
import com.ql.ecommerce_backend.dto.request.AddressRequest;
import com.ql.ecommerce_backend.dto.response.AddressResponse;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAllAddresses() {
        ApiResponse<List<AddressResponse>> result = addressService.getAllAddresses();
        return ResponseEntity.status(result.getHttpStatusCode()).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddressById(@PathVariable Long id) {
        ApiResponse<AddressResponse> address = addressService.getAddressById(id);
        return ResponseUtil.success("Address retrieved successfully", address);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(@RequestBody AddressRequest request) {
        ApiResponse<AddressResponse> newAddress = addressService.createAddress(request);
        return ResponseUtil.success("Address created successfully", newAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequest request) {
        ApiResponse<AddressResponse> updatedAddress = addressService.updateAddress(id, request);
        return ResponseUtil.success("Address updated successfully", updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseUtil.success("Address deleted successfully");
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(@PathVariable Long id) {
        ApiResponse<AddressResponse> defaultAddress = addressService.setDefaultAddress(id);
        return ResponseUtil.success("Address set as default successfully", defaultAddress);
    }
}
