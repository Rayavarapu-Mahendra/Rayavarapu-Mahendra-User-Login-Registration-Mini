package com.example.ulrs.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
	public static void main(String[] args) {
	 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	   String encodedPassword = passwordEncoder.encode("admin");
	   System.out.println(encodedPassword);
	}
}
