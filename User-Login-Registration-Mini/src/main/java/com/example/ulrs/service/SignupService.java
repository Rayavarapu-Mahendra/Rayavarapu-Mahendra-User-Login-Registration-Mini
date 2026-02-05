package com.example.ulrs.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ulrs.entity.Role;
import com.example.ulrs.entity.User;
import com.example.ulrs.repository.RoleRepository;
import com.example.ulrs.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SignupService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UniqueEmailMobileNumberService uniqueEmailMobileNumberService;
    public SignupService(UserRepository userRepository,
            RoleRepository roleRepository,
            OtpService otpService, BCryptPasswordEncoder passwordEncoder,
            UniqueEmailMobileNumberService uniqueEmailMobileNumberService) {
    			this.userRepository = userRepository;
    			this.roleRepository = roleRepository;
    			this.otpService = otpService;
    			this.passwordEncoder=passwordEncoder;
    			this.uniqueEmailMobileNumberService=uniqueEmailMobileNumberService;
    		}
    
	  public void signup(User user) {
		  String email=user.getEmail().toLowerCase().trim();
		  String mobile=user.getMobileNumber().trim();
		  uniqueEmailMobileNumberService.uniqueEmailMobile(email, mobile);
		    user.setEmail(email);
		    user.setMobileNumber(mobile);
		  
		  user.setPassword(passwordEncoder.encode(user.getPassword()));
		  user.setEnabled(false);
		  
		  Role userRole=roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
		  user.getRoles().add(userRole);
		  userRepository.save(user);
		    otpService.generateOtp(user.getEmail(), OtpPurpose.SIGNUP);
		    	}
		    
		    
	 public void verifySignupOtp(String email, String otp) {

		   otpService.verifyOtp(email, otp, OtpPurpose.SIGNUP);
	        User user = userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        
		    user.setEnabled(true);
		    userRepository.save(user);
	 }
	 
	 public void resendSignupOtp(String email) {
		 User user=userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		    if (user.isEnabled()) {
		        throw new RuntimeException("Account already verified");
		    }

		    otpService.resendOtp(email, OtpPurpose.SIGNUP);
	 }

}
