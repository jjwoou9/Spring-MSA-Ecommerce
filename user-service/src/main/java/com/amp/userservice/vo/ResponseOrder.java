package com.amp.userservice.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ResponseOrder {
	//주문정보
	private String productId;
	private Integer qty;
	private Integer unitPrice;
	private Integer totalPrice;
	private Date createdAt;
	private String orderId;
}
