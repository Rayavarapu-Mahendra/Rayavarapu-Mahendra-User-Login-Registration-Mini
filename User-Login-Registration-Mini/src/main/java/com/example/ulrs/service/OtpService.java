package com.example.ulrs.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.ulrs.entity.OtpVerification;
import com.example.ulrs.repository.OtpVerificationRepository;

@Service
public class OtpService {
	private final OtpVerificationRepository otpRepository;
	
	private final EmailService emailService;

	public OtpService(OtpVerificationRepository otpRepository,
	                  EmailService emailService) {
	    this.otpRepository = otpRepository;
	    this.emailService = emailService;
	}

	
	public String generateOtp(String email, OtpPurpose purpose) {

	    String otp = String.valueOf(100000 + new Random().nextInt(900000));

	    OtpVerification otpEntity = new OtpVerification();
	    otpEntity.setEmail(email);
	    otpEntity.setOtp(otp);
	    otpEntity.setPurpose(purpose.name());
	    otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
	    otpEntity.setVerified(false);

	    otpRepository.save(otpEntity);

	    // 🔥 Send OTP via email
	    emailService.sendOtpEmail(email, otp);

	    return otp;
	}

	
	
	 public boolean verifyOtp(String email, String otp, OtpPurpose purpose) {

	        OtpVerification otpEntity = otpRepository
	            .findTopByEmailAndPurposeAndVerifiedFalseOrderByCreatedAtDesc(
	                email, purpose.name()
	            )
	            .orElseThrow(() -> new RuntimeException("OTP not found"));

	        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
	            throw new RuntimeException("OTP expired");
	        }

	        if (!otpEntity.getOtp().equals(otp)) {
	            throw new RuntimeException("Invalid OTP");
	        }

	        otpEntity.setVerified(true);
	        otpRepository.save(otpEntity);

	        return true;
	    }
	 


	
}
