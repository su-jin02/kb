package com.unicorn.store.data;

import com.unicorn.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    User findByNickname(String nickname);
    Optional<User> findUserByEmail(String email);
    Optional<User> findByJwt(String refreshToken);
}
