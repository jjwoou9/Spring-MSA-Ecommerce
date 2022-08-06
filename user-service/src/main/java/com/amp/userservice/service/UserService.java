package com.amp.userservice.service;


import com.amp.userservice.dto.UserDto;
import com.amp.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	//회원가입 처리
	UserDto createUser(UserDto userDto);
	
	//회원 정보 조회
	UserDto getUserByUserId(String userId);
	
	//전체 회원 조회
	Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String userName);
}
