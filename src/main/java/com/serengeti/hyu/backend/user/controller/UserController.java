package com.serengeti.hyu.backend.user.controller;

import com.serengeti.hyu.backend.user.dto.LoginDto;
import com.serengeti.hyu.backend.user.dto.SignUpDto;
import com.serengeti.hyu.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //회원가입
    @GetMapping("/signup")
    public ResponseEntity<SignUpDto> signupForm() {
        SignUpDto signUpDto = new SignUpDto();
        return ResponseEntity.ok(signUpDto);
    }

    //회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpDto signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        try {
            String token = userService.login(loginDto);
            return ResponseEntity.ok(token); //jwt 토큰
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    //아이디 찾기
    @PostMapping("/forgot-id")
    public ResponseEntity<String> forgotId(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        try {
            userService.sendIdToEmail(name, email);
            return ResponseEntity.ok("아이디가 이메일로 발송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //비밀번호 찾기
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String name = request.get("name");
        String email = request.get("email");
        try {
            userService.sendTemporaryPassword(username, name, email);
            return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    //비밀번호 변경
//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
//        String username = request.get("username");
//        String oldPassword = request.get("oldPassword");
//        String newPassword = request.get("newPassword");
//        try {
//            userService.changePassword(username, oldPassword, newPassword);
//            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
    
    //소셜로그인
    @GetMapping("/loginSuccess")
    public ResponseEntity<String> loginSuccess(@AuthenticationPrincipal OAuth2User oauthUser) {
        return ResponseEntity.ok("OAuth2 로그인 성공");
    }

    
}

