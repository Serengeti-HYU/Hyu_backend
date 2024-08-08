package com.serengeti.hyu.backend.news.controller;

import com.serengeti.hyu.backend.news.dto.NewsRequest;
import com.serengeti.hyu.backend.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NewsController {

    private final NewsService newsService;

    // 뉴스레터 생성
    @PostMapping("/news-letter")
    public ResponseEntity<String> createNews(@RequestBody NewsRequest newsRequest) {
        return new ResponseEntity<>(newsService.createNewsLetter(newsRequest), HttpStatus.OK);
    }
}
