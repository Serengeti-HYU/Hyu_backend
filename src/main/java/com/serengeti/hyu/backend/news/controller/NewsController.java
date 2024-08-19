package com.serengeti.hyu.backend.news.controller;

import com.serengeti.hyu.backend.news.dto.NewsRequest;
import com.serengeti.hyu.backend.news.entity.News;
import com.serengeti.hyu.backend.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/news-letter")
public class NewsController {

    private final NewsService newsService;

    // 뉴스레터 생성
    @PostMapping
    public ResponseEntity<String> createNews(@RequestBody NewsRequest newsRequest) {
        return new ResponseEntity<>(newsService.createNewsLetter(newsRequest), HttpStatus.CREATED);
    }

    // 뉴스레터 목록 조회
    @GetMapping
    public ResponseEntity<List<News>> getNewsList() {
        List<News> newsList = newsService.getNewsLetterList();
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    // 뉴스레터 상세 조회
    @GetMapping("/{news_id}")
    public ResponseEntity<News> getNews(@PathVariable Long news_id) {
        News news = newsService.getNewsLetter(news_id);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // 뉴스레터 수정
    @PatchMapping("/{news_id}")
    public ResponseEntity<News> updateNews(@PathVariable Long news_id, @RequestBody NewsRequest newsRequest) {
        News updatedNews = newsService.updateNewsLetter(news_id, newsRequest);
        return new ResponseEntity<>(updatedNews, HttpStatus.OK);
    }

    // 뉴스레터 삭제
    @DeleteMapping("/{news_id}")
    public ResponseEntity<String> deleteNews(@PathVariable Long news_id) {
        return new ResponseEntity<>(newsService.deleteNewsLetter(news_id), HttpStatus.OK);
    }
}
