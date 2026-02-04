package com.example.ulrs.entity;

import java.time.LocalDate;


import java.util.HashSet;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message="username should not be blank")
	private String username;
	
	@NotBlank(message="First name should not be blank")
	@Size(max=25, message="First Name length less than 25 characters")
	private String firstName;
	
	@NotBlank(message="Last name should not be blank")
	@Size(max=25, message="Last Name length less than 25 characters")
	private String lastName;
	
	@Column(unique=true, nullable=false)
	@NotBlank(message="Email should not be blank")
	@Email(message="Invalid Email formate")
	private String email;
	
	@Column(unique=true, nullable=false)
	@NotNull(message="Mobile number should not be empty")
	private String mobileNumber;
	
	@Column(name="date_of_birth")
	@NotNull(message="Date Of Birth should not be blank")
	@Past(message="date Of Birth must be in the past")
	private LocalDate dateOfBirth;
	
	@NotBlank(message="gender should not be blank")
	private String gender;
	
	@NotBlank(message="Password should not be blank")
    private String password;

    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // getters & setters
}
