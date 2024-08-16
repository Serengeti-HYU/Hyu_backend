package com.serengeti.hyu.backend.rest.service;

import com.serengeti.hyu.backend.rest.entity.Rest;
import com.serengeti.hyu.backend.rest.entity.Scrap;
import com.serengeti.hyu.backend.rest.repository.ScrapRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    // 쉼활동 저장
    public void addScrap(User user, Rest rest) {

        // 중복 여부 확인
        Scrap existingScrap = scrapRepository.findByUserAndRest(user, rest);

        if (existingScrap != null) {
            throw new IllegalArgumentException("해당 사용자가 이미 이 쉼활동을 스크랩하였습니다.");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(user);
        scrap.setRest(rest);
        scrapRepository.save(scrap);
    }

    // 쉼활동 취소
    public void removeScrap(User user, Rest rest) {
        Scrap scrap = scrapRepository.findByUserAndRest(user, rest);
        if (scrap != null) {
            scrapRepository.delete(scrap);
        }
    }

    // 사용자 쉼활동 목록조회 <- 마이페이지에서 사용
    public List<Scrap> getScrapByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return scrapRepository.findByUser(user);
    }
}

