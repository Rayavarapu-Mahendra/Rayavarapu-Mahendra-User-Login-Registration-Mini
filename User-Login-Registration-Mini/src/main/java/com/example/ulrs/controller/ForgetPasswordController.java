package com.example.ulrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ulrs.service.OtpPurpose;
import com.example.ulrs.service.OtpService;
import com.example.ulrs.service.ForgetPasswordService;

@Controller
public class ForgetPasswordController {
	
    private final ForgetPasswordService forgetPasswordService;

    public ForgetPasswordController(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }

    @GetMapping("/forgot-password")
    public String forgetPasswordPage() {
    	return "forgot-password";
    }
    
    
    
    @PostMapping("/change-forget-password")
    public String forgotPassword(@RequestParam String email) {
    	System.out.println("controller1");
    	forgetPasswordService.forgotPassword(email); //1
    	System.out.println("controller2");
        return "redirect:/enterotp?email=" + email + "&purpose=FORGOT_PASSWORD";
    }

    @GetMapping("/enterotp")
    public String otpPage(@RequestParam String email, @RequestParam String purpose, Model model) {
    	model.addAttribute("email", email);
    	model.addAttribute("purpose", purpose);
    	System.out.println("controller3");
        return "otp-verify-forgot-password";
    }
    
    
    @PostMapping("/verify-forgot-otp")
    public String verifyForgotOtp(@RequestParam String email,
                                  @RequestParam String otp) {
    	System.out.println("controller4");
    
    	forgetPasswordService.verifyForgotPasswordOtp(email, otp);
    	System.out.println("controller5");
        return "redirect:/reset-user-password?email=" + email;
    }
    
    @GetMapping("/reset-user-password")
    public String resetPasswordPage(@RequestParam String email, Model model) {
    	System.out.println("controller-reset-page");
        model.addAttribute("email", email);
        return "reset-password";
    }    
    
    @PostMapping("/reset-user-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password) {
    	System.out.println("controller6");
    	forgetPasswordService.resetPassword(email, password);
        return "redirect:/login?resetSuccess";
    }
    @PostMapping("/resend-forgot-otp")
    public String resendOtp(@RequestParam String email) {

        forgetPasswordService.resendForgotPasswordOtp(email);

        return "redirect:/enterotp?email=" + email + "&purpose=FORGOT_PASSWORD";
    }


}
