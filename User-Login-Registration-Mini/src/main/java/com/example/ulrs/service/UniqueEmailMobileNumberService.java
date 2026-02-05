package com.example.ulrs.service;

import org.springframework.stereotype.Service;

import com.example.ulrs.repository.UniqueEmailMobileNumber;

@Service
public class UniqueEmailMobileNumberService {
	UniqueEmailMobileNumber uniqueEmailMobileNumber;
	
	public UniqueEmailMobileNumberService(UniqueEmailMobileNumber uniqueEmailMobileNumber) {
		this.uniqueEmailMobileNumber=uniqueEmailMobileNumber;
	}
	
	
	public void uniqueEmailMobile(String email, String mobilenumber) {
	   	if (uniqueEmailMobileNumber.existsByEmail(email)) {
		    throw new RuntimeException("Email already registered");
		}
		if(uniqueEmailMobileNumber.existsByMobileNumber(mobilenumber)) {
			throw new RuntimeException("Mobile Number already registered");
		}
	}
	

}
