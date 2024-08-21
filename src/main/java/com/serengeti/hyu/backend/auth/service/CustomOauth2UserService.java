package com.serengeti.hyu.backend.auth.service;

import com.serengeti.hyu.backend.auth.dto.OAuth2UserInfo;
import com.serengeti.hyu.backend.auth.dto.kakao.KakaoUserDetails;
import com.serengeti.hyu.backend.config.JwtTokenUtil;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail(); // Email may be null
        String name = oAuth2UserInfo.getName();
        String username = provider + "_" + providerId;

        log.info("Provider: {}, ProviderId: {}, Email: {}, Name: {}, Username: {}", provider, providerId, email, name, username);

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(username)
                            .name(name)
                            .email(email) // email can be null
                            .provider(provider)
                            .providerId(providerId)
                            .build();

                    User savedUser = userRepository.save(newUser);


                    return savedUser;
                });

        System.out.println("카카오 유저 정보: " + oAuth2User.getAttributes());

        return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
    }
}
