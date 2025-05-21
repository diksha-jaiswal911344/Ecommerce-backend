package com.ql.ecommerce_backend.repository;

import com.ql.ecommerce_backend.entity.User;
import com.ql.ecommerce_backend.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUser(User user);
    Optional<UserAddress> findByUserAndIsDefaultTrue(User user);
}