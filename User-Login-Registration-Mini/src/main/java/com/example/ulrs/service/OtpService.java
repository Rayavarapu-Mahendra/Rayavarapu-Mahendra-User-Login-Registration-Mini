package com.example.ulrs.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ulrs.entity.OtpVerification;
import com.example.ulrs.repository.OtpVerificationRepository;

@Service
@Transactional
public class OtpService {
	private final OtpVerificationRepository otpRepository;
	
	private final EmailService emailService;
	 private final SecureRandom secureRandom = new SecureRandom();
	 private final PasswordEncoder passwordEncoder;
	public OtpService(OtpVerificationRepository otpRepository,
	                  EmailService emailService, PasswordEncoder passwordEncoder) {
	    this.otpRepository = otpRepository;
	    this.emailService = emailService;
	    this.passwordEncoder=passwordEncoder;
	}

	
	
	public String generateOtp(String email, OtpPurpose purpose) {

		email=email.trim().toLowerCase();
		
		otpRepository.invalidatePreviousOtps(email, purpose.name());
		
	    String otp = String.valueOf(100000 + secureRandom.nextInt(900000));

	    OtpVerification otpEntity = new OtpVerification();
	    otpEntity.setEmail(email);
	    otpEntity.setOtp(hashOtp(otp));
	    otpEntity.setPurpose(purpose.name());
	    otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
	    otpEntity.setVerified(false);

	    otpRepository.save(otpEntity);

	    // 🔥 Send OTP via email
	    emailService.sendOtpEmail(email, otp);
	    return otp;
	}

	
	
	public void verifyOtp(String email, String otp, OtpPurpose purpose) {

	    email = email.trim().toLowerCase();

	    OtpVerification otpEntity = otpRepository
	        .findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
	            email, purpose.name()
	        )
	        .orElseThrow(() -> new RuntimeException("OTP not found"));

	    if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP expired");
	    }

	    if (!otpEntity.getOtp().equals(hashOtp(otp))) {
	        throw new RuntimeException("Invalid OTP");
	    }

	    otpEntity.setVerified(true);
	    otpRepository.save(otpEntity);
	}

	 
	 
	 public void resendOtp(String email, OtpPurpose purpose) {
		 email = email.trim().toLowerCase();
		 
		    otpRepository
	        .findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
	            email, purpose.name()
	        )
	        .ifPresent(otp -> {
	            throw new RuntimeException("OTP already verified");
	        });
		 
		 otpRepository.findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(email, purpose.name())
		 .ifPresent(lastOtp -> {
			 if(lastOtp.getCreatedAt()
					 .isAfter(LocalDateTime.now().minusSeconds(60))) {
				 throw new RuntimeException("Pleade wait 60 seconds before requesting a new OTP");
			 }
		 });
		 otpRepository.invalidatePreviousOtps(email,purpose.name());
		 generateOtp(email, purpose);
		 
	 }

	 private String hashOtp(String otp) {
		    try {
		        MessageDigest md = MessageDigest.getInstance("SHA-256");
		        byte[] hash = md.digest(otp.getBytes(StandardCharsets.UTF_8));
		        return HexFormat.of().formatHex(hash);
		    } catch (NoSuchAlgorithmException e) {
		        throw new RuntimeException("SHA-256 not supported", e);
		    }
		}

	
}
