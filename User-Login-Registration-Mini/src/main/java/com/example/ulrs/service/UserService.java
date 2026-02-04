package com.example.ulrs.service;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ulrs.service.OtpPurpose;

import com.example.ulrs.entity.Role;
import com.example.ulrs.entity.User;
import com.example.ulrs.repository.RoleRepository;
import com.example.ulrs.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            OtpService otpService) {
    			this.userRepository = userRepository;
    			this.roleRepository = roleRepository;
    			this.otpService = otpService;
    		}
    
    
    
    public void resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
    
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        otpService.generateOtp(email, OtpPurpose.FORGOT_PASSWORD);
    }

    
    
    public void signup(String username, String email, String password, String mobilenumber, String firstname, String lastname, LocalDate dateofbirth, String gender) {
    	
    	if (userRepository.existsByEmail(email)) {
    	    throw new RuntimeException("Email already registered");
    	}
    	if(userRepository.existsByMobileNumber(mobilenumber)) {
    		throw new RuntimeException("Email already registered");
    	}
    	
    User user = new User();
    user.setUsername(username);
    user.setFirstName(firstname);
    user.setLastName(lastname);
    user.setEmail(email);
    user.setMobileNumber(mobilenumber);
    user.setDateofbirth(dateofbirth);
    user.setGender(gender);
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(false);

    Role userRole = roleRepository
            .findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

    user.getRoles().add(userRole);

    userRepository.save(user);

    otpService.generateOtp(email, OtpPurpose.SIGNUP);
    	}
    
    
    public void verifySignupOtp(String email, String otp) {

        otpService.verifyOtp(email, otp, OtpPurpose.SIGNUP);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
    }



}
