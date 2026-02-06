package com.example.ulrs.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "otp_verification",
		indexes = {
        @Index(name = "idx_otp_email_purpose", columnList = "email,purpose"),
        @Index(name = "idx_otp_verified", columnList = "verified")
    }
)
public class OtpVerification {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(nullable = false)
		private String email;
		
		@Column(nullable = false, length = 64)
		private String otp;
		
		@Column(nullable = false)
		private String purpose;
		
		@Column(nullable = false)
		private LocalDateTime expiresAt;
		
		@Column(nullable = false)
		private boolean verified = false;
		
	    @CreatedDate
	    @Column(nullable = false, updatable = false)
		private LocalDateTime createdAt;
}
