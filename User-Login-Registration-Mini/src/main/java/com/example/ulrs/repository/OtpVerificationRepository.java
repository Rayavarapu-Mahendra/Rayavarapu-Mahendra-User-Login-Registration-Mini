package com.example.ulrs.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ulrs.entity.OtpVerification;

@Repository
public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification>
    findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
            String email,
            String purpose
    );

    @Modifying
    @Query("""
        UPDATE OtpVerification o
        SET o.verified = true
        WHERE o.email = :email
          AND o.purpose = :purpose
          AND o.verified = false
    """)
    void invalidatePreviousOtps(@Param("email") String email,
                                @Param("purpose") String purpose);

    void deleteByExpiresAtBefore(LocalDateTime time);
}
