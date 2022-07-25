package com.amp.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

	@NotNull(message="이메일 입력은 필수 입니다.")
	@Size(min=5, message="이메일은 최소 5글자 이상입니다.")
	@Email
	private String email;

	@Size(min=2, message="이름은 최소 2글자 이상입니다.")
	@NotNull(message="이름 입력은 필수 입니다.")
	private String name;

	@NotNull(message="비밀번호 입력은 필수 입니다.")
	@Size(min=2, message="비밀번호는 최소 2글자 이상입니다.")
	private String pwd;
}
