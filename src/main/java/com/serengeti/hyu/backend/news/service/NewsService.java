package com.serengeti.hyu.backend.news.service;

import com.serengeti.hyu.backend.news.entity.News;
import com.serengeti.hyu.backend.news.dto.NewsRequest;
import com.serengeti.hyu.backend.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    public String createNewsLetter(NewsRequest request) {

        News news = new News();
        news.setTitle(request.getTitle());
        news.setDate(LocalDate.now());
        news.setContent(request.getContent());
        news.setLink(request.getLink());

        // REVIEW 엔티티 저장
        newsRepository.save(news);

        return "뉴스레터 생성 완료";
    }
}
