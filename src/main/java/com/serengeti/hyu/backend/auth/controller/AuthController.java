package com.serengeti.hyu.backend.auth.controller;

import com.serengeti.hyu.backend.auth.dto.kakao.KakaoApiClient;
import com.serengeti.hyu.backend.auth.dto.kakao.KakaoInfoResponse;
import com.serengeti.hyu.backend.config.JwtTokenUtil;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import com.serengeti.hyu.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;

    @GetMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("access_token") String accessToken) {
        KakaoInfoResponse kakaoInfo = kakaoApiClient.getUserInfo(accessToken);


        System.out.println("Kakao API Response 정보: " + kakaoInfo);

        if (kakaoInfo.getKakaoAccount() == null && (kakaoInfo.getProperties() == null || kakaoInfo.getProperties().getNickname() == null)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카카오 계정 정보를 찾을 수 없습니다.");
        }
        KakaoInfoResponse.KakaoAccount kakaoAccount = kakaoInfo.getKakaoAccount();

        String email = kakaoAccount != null ? kakaoAccount.getEmail() : null;
        String providerId = kakaoInfo.getId();
        String name = kakaoInfo.getProperties() != null ? kakaoInfo.getProperties().getNickname() : "Unknown";
        String username = "kakao_" + providerId;

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(username)
                            .name(name)
                            .email(email)
                            .provider("kakao")
                            .providerId(providerId)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok("Bearer " + token);
    }
}

