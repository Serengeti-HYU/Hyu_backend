package com.serengeti.hyu.backend.news.service;

import com.serengeti.hyu.backend.news.entity.News;
import com.serengeti.hyu.backend.news.dto.NewsRequest;
import com.serengeti.hyu.backend.news.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

        newsRepository.save(news);

        return "뉴스레터 생성 완료\nnews_id : " + news.getNewsId();
    }

    // 뉴스레터 목록
    public List<News> getNewsLetterList() {
        return newsRepository.findAll();

    }

    // 뉴스레터 상세 조회
    public News getNewsLetter(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("뉴스래터를 찾을 수 없습니다."));
    }

    // 누스레터 수정
    public News updateNewsLetter(Long newsId, NewsRequest request) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("뉴스레터를 찾을 수 없습니다."));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setLink(request.getLink());

        newsRepository.save(news);
        return news;
    }

    // 뉴스레터 삭제
    public String deleteNewsLetter(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("뉴스레터를 찾을 수 없습니다."));

        newsRepository.delete(news);
        return "뉴스레터가 삭제되었습니다.\nnews_id: " + newsId;
    }
}
