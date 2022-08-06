package com.amp.userservice.jpa;

import com.amp.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUserId(String userId);
//	UserEntity findByEmail(String username);

	User findByUsername(String username);

	Optional<User> findByEmail(String email);

	// SELECT * FROM user WHERE provider = ?1 and providerId = ?2
	Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
