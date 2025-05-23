package com.ql.ecommerce_backend.services.impl;

import com.ql.ecommerce_backend.dto.mapper.AddressMapper;
import com.ql.ecommerce_backend.dto.request.AddressRequest;
import com.ql.ecommerce_backend.dto.response.AddressResponse;
import com.ql.ecommerce_backend.dto.response.ApiResponse;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.UserAddress;
import com.ql.ecommerce_backend.exceptions.ResourceNotFoundException;
import com.ql.ecommerce_backend.repository.UserAddressRepository;
import com.ql.ecommerce_backend.repository.UserRepository;
import com.ql.ecommerce_backend.services.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final UserAddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(UserAddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public ApiResponse<List<AddressResponse>> getAllAddresses() {
        User currentUser = getCurrentUser();
        List<UserAddress> addresses = addressRepository.findByUser(currentUser);
        return ApiResponse.success(200, true, "Addresses fetched successfully", addressMapper.toResponseList(addresses));
    }

    @Override
    public ApiResponse<AddressResponse> getAddressById(Long id) {
        User currentUser = getCurrentUser();
        UserAddress address = addressRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return ApiResponse.success(200, true, "Address fetched successfully", addressMapper.toResponse(address));
    }

    @Override
    @Transactional
    public ApiResponse<AddressResponse> createAddress(AddressRequest request) {
        User currentUser = getCurrentUser();

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.resetDefaultAddresses(currentUser.getId());
        }

        UserAddress address = addressMapper.toEntity(request, currentUser);
        UserAddress savedAddress = addressRepository.save(address);
        return ApiResponse.success(201, true, "Address created successfully", addressMapper.toResponse(savedAddress));
    }

    @Override
    @Transactional
    public ApiResponse<AddressResponse> updateAddress(Long id, AddressRequest request) {
        User currentUser = getCurrentUser();
        UserAddress address = addressRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(address.getIsDefault())) {
            addressRepository.resetDefaultAddresses(currentUser.getId());
        }

        addressMapper.updateEntityFromRequest(address, request);
        UserAddress updatedAddress = addressRepository.save(address);
        return ApiResponse.success(200, true, "Address updated successfully", addressMapper.toResponse(updatedAddress));
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteAddress(Long id) {
        User currentUser = getCurrentUser();
        UserAddress address = addressRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        addressRepository.delete(address);
        return ApiResponse.success(200, true, "Address deleted successfully", "Deleted");
    }

    @Override
    @Transactional
    public ApiResponse<AddressResponse> setDefaultAddress(Long id) {
        User currentUser = getCurrentUser();

        addressRepository.resetDefaultAddresses(currentUser.getId());

        UserAddress address = addressRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        address.setIsDefault(true);
        UserAddress updatedAddress = addressRepository.save(address);
        return ApiResponse.success(200, true, "Default address set successfully", addressMapper.toResponse(updatedAddress));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
