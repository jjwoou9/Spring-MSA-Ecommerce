package com.amp.userservice.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//null이 아닌 데이터만 반환하도록
public class ResponseUser {
	//회원가입 후에는 회원의 이메잃, 이름 아이디를 반환
	private String email;
	private String name;
	private String userId;
	
	//주문정보
	private List<ResponseOrder> orders; 

}
