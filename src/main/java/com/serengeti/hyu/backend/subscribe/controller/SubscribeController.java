package com.serengeti.hyu.backend.subscribe.controller;

import com.serengeti.hyu.backend.subscribe.dto.SubscribeDto;
import com.serengeti.hyu.backend.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscribeService newsService;

    // 뉴스레터 구독
    @PostMapping("/{user_name}")
    public ResponseEntity<String> subscribeNews(@PathVariable("user_name") String userName, @RequestBody SubscribeDto request) {
        return new ResponseEntity<>(newsService.subscribeNewsLetter(userName, request), HttpStatus.OK);
    }

}
