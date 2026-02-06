package com.example.ulrs.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ulrs.entity.User;
import com.example.ulrs.repository.UserRepository;

@Service
public class ForgetPasswordService {
    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    
    public ForgetPasswordService(UserRepository userRepository,
            OtpService otpService, PasswordEncoder passwordEncoder) {
    			this.userRepository = userRepository;
    			this.otpService = otpService;
    			this.passwordEncoder=passwordEncoder;
    		}
 
    public void forgotPassword(String email) {

        userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        otpService.generateOtp(email, OtpPurpose.FORGOT_PASSWORD);
    }
    
	  public void verifyForgotPasswordOtp(String email, String otp) {


		    otpService.verifyOtp(email, otp, OtpPurpose.FORGOT_PASSWORD);

		    User user = userRepository.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("User not found"));

		    user.setPasswordResetAllowed(true);
		    userRepository.save(user);
		}
    
	  public void resetPassword(String email, String newPassword) {
	
		  
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	        if(!user.isPasswordResetAllowed()) {
	        	throw new RuntimeException("OTP verification required");
	        }
	        user.setPassword(passwordEncoder.encode(newPassword));
	        user.setPasswordResetAllowed(false);
	        userRepository.save(user);
	    }
	 
	    public void resendForgotPasswordOtp(String email) {
	        otpService.resendOtp(email, OtpPurpose.FORGOT_PASSWORD);
	    }
}
