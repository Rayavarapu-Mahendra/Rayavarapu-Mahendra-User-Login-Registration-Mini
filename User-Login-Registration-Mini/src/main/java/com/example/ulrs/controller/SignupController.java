package com.example.ulrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ulrs.entity.User;
import com.example.ulrs.service.SignupService;
import jakarta.validation.Valid;

@Controller
public class SignupController {
	
	private final SignupService signupService;
	public SignupController(SignupService signupService) {
		this.signupService=signupService;
	}
	
	
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("formValidate", new User());
		return "signup";
	}
	
	@PostMapping("/saveUserDetails")
	public String saveUserDetails(@Valid @ModelAttribute("formValidate") User user, BindingResult result, Model model) {

		if(result.hasErrors()) {
			return "signup";
		}

        try {
            signupService.signup(user); 
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "signup";
        }


		return "redirect:/otp?email=" + user.getEmail();
	}
	
    @GetMapping("/otp")
    public String otpPage(@RequestParam String email, Model model) {
    	model.addAttribute("email", email);
        return "otp-verify";
    }
	
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp, Model model) {
    	
    	try {
    		signupService.verifySignupOtp(email, otp);
    	}catch(RuntimeException ex) {
    		model.addAttribute("error", ex.getMessage());
    		model.addAttribute("email", email);
    		return "otp-verify";
    	}
        return "redirect:/login";
    }
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String email, Model model) {
    		try {
    	        signupService.resendSignupOtp(email);
    	        model.addAttribute("success", "OTP resent successfully");
    		}catch(RuntimeException ex) {
    			model.addAttribute("error", ex.getMessage());
    		}
    	    model.addAttribute("email", email);
    	    return "otp-verify";
    }

}
