package com.serengeti.hyu.backend.user.repository;

import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //username 사용자 찾기
    Optional<User> findByUsername(String username);

    //이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

}
