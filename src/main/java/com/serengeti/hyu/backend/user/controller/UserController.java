package com.serengeti.hyu.backend.user.controller;

import com.serengeti.hyu.backend.user.dto.SignUpDto;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    //로그인
    
}
