package com.amp.orderservice.service;

import com.amp.orderservice.dto.OrderDto;
import com.amp.orderservice.jpa.OrderEntity;

public interface OrderService {
	OrderDto createOrder(OrderDto orderDto);
	OrderDto getOrderByOrderId(String orderId);
	Iterable<OrderEntity> getOrdersByUserId(String userId);
}
