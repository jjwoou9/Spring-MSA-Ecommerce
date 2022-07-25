package com.amp.orderservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
	private String productId;
	private Integer qty;
	private Integer unitPrice;
	private Integer totalPrice;
	
	private String orderId;
	private String userId;
}
