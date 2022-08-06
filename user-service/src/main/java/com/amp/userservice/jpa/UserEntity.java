package com.amp.userservice.jpa;

import javax.persistence.*;

import com.amp.userservice.model.Role;

import java.io.Serializable;

@Deprecated
public class UserEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 50, unique = true)
	private String email;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String userId;
	
	@Column(nullable = false)
	private String encryptedPwd;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
}
