package com.serengeti.hyu.backend.user.service;

import com.serengeti.hyu.backend.user.dto.LoginDto;
import com.serengeti.hyu.backend.user.dto.SignUpDto;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.config.JwtTokenUtil;
import com.serengeti.hyu.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final JavaMailSender mailSender;

    //회원가입
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

    //로그인
    public String login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenUtil.generateToken(user);
    }

    //아이디 찾기
    public void sendIdToEmail(String name, String email) {
        User user = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름과 이메일로 등록된 사용자가 없습니다."));

        String username = user.getUsername();
        sendFindEmail(user.getEmail(), "휴 아이디 찾기", "고객님의 아이디는 " + username + "입니다.");
    }

    private void sendFindEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
