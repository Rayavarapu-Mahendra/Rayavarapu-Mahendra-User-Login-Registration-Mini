package com.example.ulrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/otp")
    public String otpPage() {
        return "otp-verify";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
