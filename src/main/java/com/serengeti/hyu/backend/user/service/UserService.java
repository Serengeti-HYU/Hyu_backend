package com.serengeti.hyu.backend.user.service;

import com.serengeti.hyu.backend.user.dto.LoginDto;
import com.serengeti.hyu.backend.user.dto.SignUpDto;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpDto signUpDto){

        String encryptedPassword = passwordEncoder.encode(signUpDto.getPassword());
        User newUser = User.builder()
                .name(signUpDto.getName())
                .birth(signUpDto.getBirth())
                .username(signUpDto.getUsername())
                .password(encryptedPassword)
                .phoneNumber(signUpDto.getPhoneNumber())
                .email(signUpDto.getEmail())
                .build();

        userRepository.save(newUser);
    }

    public String login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return "로그인 성공";
    }


}
