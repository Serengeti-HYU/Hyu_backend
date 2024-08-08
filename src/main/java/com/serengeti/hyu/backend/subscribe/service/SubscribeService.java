package com.serengeti.hyu.backend.subscribe.service;

import com.serengeti.hyu.backend.subscribe.dto.SubscribeDto;
import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import com.serengeti.hyu.backend.subscribe.repository.SubscribeRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    public String subscribeNewsLetter(String userName, SubscribeDto request) {

        // 사용자 확인
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 존재 O
        Subscribe subscribe = new Subscribe();
        subscribe.setDate(Collections.singletonList(LocalDate.now()));
        subscribe.setUser(user);
        subscribe.setEmail(request.getEmail());

        subscribeRepository.save(subscribe);
        return "구독 완료";
    }
}