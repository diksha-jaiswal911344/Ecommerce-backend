package com.ql.ecommerce_backend.dto.mapper;

import com.ql.ecommerce_backend.dto.request.AddressRequest;
import com.ql.ecommerce_backend.dto.response.AddressResponse;
import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.UserAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddressMapper {
    public UserAddress toEntity(AddressRequest request, User user) {
        UserAddress address = new UserAddress();
        address.setUser(user);
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());
        address.setAddressType(request.getAddressType());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        return address;
    }

    public AddressResponse toResponse(UserAddress address) {
        return new AddressResponse(
                address.getId(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZipCode(),
                address.getAddressType(),
                address.getIsDefault()
        );
    }

    public List<AddressResponse> toResponseList(List<UserAddress> addresses) {
        return addresses.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(UserAddress address, AddressRequest request) {
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());
        address.setAddressType(request.getAddressType());
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }
    }
}
