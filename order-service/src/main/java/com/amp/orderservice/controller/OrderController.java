package com.amp.orderservice.controller;

import java.util.ArrayList;
import java.util.List;

import com.amp.orderservice.dto.OrderDto;
import com.amp.orderservice.jpa.OrderEntity;
import com.amp.orderservice.service.OrderService;
import com.amp.orderservice.vo.RequestOrder;
import com.amp.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order-service")
@RestController
public class OrderController {
	Environment env;
	OrderService orderService;
	
	@Autowired
	public OrderController(Environment env, OrderService orderService) {
		super();
		this.env = env;
		this.orderService = orderService;
	}



	@GetMapping("/health_check")
	public String healthCheck() {
		// 할당받은 포트번호 출력
		return String.format("OK ... Port num = %s", env.getProperty("local.server.port"));
	}
	
	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder order){
		ModelMapper mapper=new ModelMapper();
		// ResponseOrder -> OrderDto
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		OrderDto orderDto =mapper.map(order, OrderDto.class);
		
		// 본문에 저장되지 않은 유저 정보 저장
		orderDto.setUserId(userId);
		// create 서비스 호출
		OrderDto resultDto=orderService.createOrder(orderDto);
		
		//OrderDto -> ResponseOrder
		ResponseOrder responseOrder=mapper.map(resultDto, ResponseOrder.class); 
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}
	
	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId){
		Iterable<OrderEntity> orderList= orderService.getOrdersByUserId(userId);
		
		List<ResponseOrder> resultList=new ArrayList<>();
		orderList.forEach(v-> resultList.add(new ModelMapper().map(v, ResponseOrder.class)));
		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}
	
	
}
