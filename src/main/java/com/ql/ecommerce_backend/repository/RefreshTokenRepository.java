package com.ql.ecommerce_backend.repository;

import com.ql.ecommerce_backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by its token value
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Delete refresh tokens for a specific user
     */
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * Find all refresh tokens for a specific user
     */
    List<RefreshToken> findAllByUserId(Long userId);

    /**
     * Find all expired tokens
     */
    List<RefreshToken> findByExpiryDateBefore(Instant now);

    /**
     * Delete all expired tokens
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < ?1")
    void deleteAllExpiredTokens(Instant now);

    /**
     * Check if a user has any valid refresh tokens
     */
    boolean existsByUserIdAndExpiryDateAfter(Long userId, Instant now);

    /**
     * Find the most recently created token for a user
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.id = ?1 ORDER BY rt.createdAt DESC")
    Optional<RefreshToken> findMostRecentByUserId(Long userId);

    /**
     * Invalidate all tokens for a user (mark as used)
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.used = true WHERE rt.user.id = ?1")
    void invalidateAllUserTokens(Long userId);
}
