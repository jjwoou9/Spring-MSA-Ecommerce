package com.amp.orderservice.service;

import java.util.UUID;

import com.amp.orderservice.dto.OrderDto;
import com.amp.orderservice.jpa.OrderEntity;
import com.amp.orderservice.jpa.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
	OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public OrderDto createOrder(OrderDto orderDto) {//주문저장
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
		
		//OrderDto -> OrderEntity
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
		
		// DB 저장
		orderRepository.save(orderEntity);
		
		return mapper.map(orderEntity, OrderDto.class);
	}

	@Override
	public OrderDto getOrderByOrderId(String orderId) {//주문 아이디를 이용한 조회
		 OrderDto orderDto = new ModelMapper().map(orderRepository.findByOrderId(orderId), OrderDto.class);
		return orderDto;
	}

	@Override
	public Iterable<OrderEntity> getOrdersByUserId(String userId) {// 회원아이디를 이용한 조회
		return orderRepository.findByUserId(userId);
	}

}
