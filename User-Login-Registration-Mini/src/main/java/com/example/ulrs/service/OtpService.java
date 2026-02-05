package com.example.ulrs.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ulrs.entity.OtpVerification;
import com.example.ulrs.repository.OtpVerificationRepository;

@Service
@Transactional
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
	 
	 
	 public void resendOtp(String email, OtpPurpose purpose) {
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


	
}
