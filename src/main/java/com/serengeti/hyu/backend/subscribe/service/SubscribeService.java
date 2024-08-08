package com.serengeti.hyu.backend.subscribe.service;

import com.serengeti.hyu.backend.news.entity.News;
import com.serengeti.hyu.backend.news.repository.NewsRepository;
import com.serengeti.hyu.backend.subscribe.SubscribeManager;
import com.serengeti.hyu.backend.subscribe.dto.SubscribeDto;
import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import com.serengeti.hyu.backend.subscribe.repository.SubscribeRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeService {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeService.class);

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;
    private final SubscribeManager subscribeManager;
    private final NewsRepository newsRepository;

    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시
    public void sendWeeklyNewsLetter() {

        // 오늘 요일
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        // 오늘 요일에 구독한 사용자 조회
        List<Subscribe> subscribers = subscribeRepository.findByDayOfWeek(today);


        for (Subscribe subscribe : subscribers) {
            String email = subscribe.getEmail();

            // 뉴스레터 전송
            sendNewsLetter(email);
        }

        // 작업 완료 로그
        logger.info("뉴스레터 전송 완료");
    }

    public String subscribeNewsLetter(String userName, SubscribeDto request) {

        // 사용자 확인
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 구독 상태 설정
        Subscribe.Status status = Subscribe.Status.ACTIVE;

        // 사용자 존재 O
        Subscribe subscribe = Subscribe.builder()
                .user(user)
                .email(request.getEmail())
                .status(status)
                .dayOfWeek(request.getDayOfWeek()) // DTO에 요일 정보가 있을 경우
                .build();


        subscribeRepository.save(subscribe);
        return "구독 완료";
    }


    public String sendNewsLetter(String email) {

        LocalDate today = LocalDate.now();
        try {
            List<News> newsLetter = newsRepository.findByDate(today);


            for(News news : newsLetter) {
                subscribeManager.send(email, news);
            }

//            NewsDto newsDto = new NewsDto();
//            newsDto.setTitle(newsLetter.getTitle());
//            newsDto.setContent(newsLetter.getContent());
//            newsDto.setLink(newsLetter.getLink());



            return "뉴스레터 전송 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Email 주소 형식 점검
    private boolean isValidEmail(String email) {
        String emailFormat = "^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailFormat);
    }
}