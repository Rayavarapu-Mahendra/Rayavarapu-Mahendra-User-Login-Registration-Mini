package com.example.ulrs.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ulrs.service.OtpPurpose;
import com.example.ulrs.service.OtpService;
import com.example.ulrs.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    // ✅ Signup
    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String mobilenumber,
                         @RequestParam String gender,
                         @RequestParam LocalDate dateofbirth,
                         @RequestParam String firstname,
                         @RequestParam String lastname ) {

        userService.signup(username, email, password, mobilenumber, lastname, firstname, dateofbirth, gender);
        return "redirect:/otp?email=" + email;
    }

    // ✅ Verify signup OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        userService.verifySignupOtp(email, otp);
        return "redirect:/login";
    }

    // ✅ Forgot password
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {

        userService.forgotPassword(email);
        return "redirect:/otp?email=" + email;
    }

    // ✅ Verify forgot-password OTP
    @PostMapping("/verify-forgot-otp")
    public String verifyForgotOtp(@RequestParam String email,
                                  @RequestParam String otp) {

        otpService.verifyOtp(email, otp, OtpPurpose.FORGOT_PASSWORD);
        return "redirect:/reset-password?email=" + email;
    }

    // ✅ Reset password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password) {

        userService.resetPassword(email, password);
        return "redirect:/login?resetSuccess";
    }
}
