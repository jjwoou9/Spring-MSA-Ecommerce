package com.amp.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amp.userservice.dto.UserDto;
import com.amp.userservice.jpa.UserEntity;
import com.amp.userservice.jpa.UserRepository;
import com.amp.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
	
	UserRepository userRepository;
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		// repository에 전달할 데이터를 추가
		userDto.setUserId(UUID.randomUUID().toString());

		// UserDto -> UserEntity
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPwd("encryped_password");

		// TODO - UserEntity에 값을 추가 - UserDto에 있는 암호화하지 않은 패스워드를 암호화해서 설정
		userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

		// 저장 - 저장 결과가 다시 userEntity에 반영
		userRepository.save(userEntity);

		// UserEntity -> UserDto
		UserDto result = mapper.map(userEntity, UserDto.class);
		return result;
	}

	@Override
	public UserDto getUserByUserId(String userId) {//해당 회원 정보 조회
		// DB에서 일치하는 조회
		UserEntity userEntity = userRepository.findByUserId(userId);
		//조회 결과 존재여부 확인
		if(userEntity==null) {
			throw new UsernameNotFoundException("일치하는 사용자 정보가 없습니다.");
		}
		// UserEntity->UserDto
		UserDto userDto= new ModelMapper().map(userEntity, UserDto.class);

		// DB에서 해당 사용자의 구매내역을 조회
		List<ResponseOrder> orders=new ArrayList();
		// DB에서 해당사용자의 구매내역을 조회 -> UserDto에 추가, 반환
		userDto.setOrders(orders);

		return userDto;
	}

	@Override
	public Iterable<UserEntity> getUserByAll() {//모든 사용자 조회
		return userRepository.findAll();
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if(userEntity == null){
			throw new UsernameNotFoundException(email);
		}

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);

		if(userEntity == null){
			throw new UsernameNotFoundException(username);
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
				true, true, true, true, new ArrayList<>());
	}
}
