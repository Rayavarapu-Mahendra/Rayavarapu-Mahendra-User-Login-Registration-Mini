package com.example.ulrs.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ulrs.entity.User;
import com.example.ulrs.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OtpService otpService;

    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository,
            OtpService otpService, PasswordEncoder passwordEncoder) {
    			this.userRepository = userRepository;
    			this.otpService = otpService;
    			this.passwordEncoder=passwordEncoder;
    		}
    
    public void resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
    
    public void forgotPassword(String email) {

        userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        otpService.generateOtp(email, OtpPurpose.FORGOT_PASSWORD);
    }

    
    
  


}
