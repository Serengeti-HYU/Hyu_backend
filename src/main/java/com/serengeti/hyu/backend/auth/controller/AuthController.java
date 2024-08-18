package com.serengeti.hyu.backend.auth.controller;

import com.serengeti.hyu.backend.auth.dto.kakao.KakaoApiClient;
import com.serengeti.hyu.backend.auth.dto.kakao.KakaoInfoResponse;
import com.serengeti.hyu.backend.auth.dto.kakao.KakaoTokens;
import com.serengeti.hyu.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

    private final UserService userService;
    private final KakaoApiClient kakaoApiClient;

//    @GetMapping("/kakao/callback")
//    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
//        try {
//            KakaoTokens tokens = kakaoApiClient.getAccessToken(code);
//            KakaoInfoResponse userInfo = kakaoApiClient.getUserInfo(tokens.getAccess_token());
//            String jwtToken = userService.kakaoLogin(userInfo);
//            return ResponseEntity.ok(jwtToken);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
//        }
//    }
}
