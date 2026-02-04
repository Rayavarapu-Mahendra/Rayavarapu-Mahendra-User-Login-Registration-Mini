package com.example.ulrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ulrs.entity.User;

public interface UniqueEmailMobileNumber extends JpaRepository<User, Long>{
	boolean existsByEmail(String email);
	boolean existsByMobileNumber(String mobileNumber);
}
