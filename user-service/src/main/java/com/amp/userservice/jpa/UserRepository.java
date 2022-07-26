package com.amp.userservice.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByUserId(String userId);
//	UserEntity findByEmail(String username);

	Optional<UserEntity> findByEmail(String email);
}
