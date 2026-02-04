package com.example.ulrs.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ulrs.entity.OtpVerification;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
            String email,
            String purpose
    );

    void deleteByExpiresAtBefore(LocalDateTime time);
}
