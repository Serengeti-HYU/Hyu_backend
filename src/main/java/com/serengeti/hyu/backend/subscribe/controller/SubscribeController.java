package com.serengeti.hyu.backend.subscribe.controller;

import com.serengeti.hyu.backend.subscribe.dto.SubscribeDto;
import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import com.serengeti.hyu.backend.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    // 뉴스레터 구독 (사용자)
    @PostMapping("/{user_name}")
    public ResponseEntity<SubscribeDto> subscribeNews(@PathVariable("user_name") String userName, @RequestBody SubscribeDto request) {
        return new ResponseEntity<>(subscribeService.subscribeNewsLetter(userName, request), HttpStatus.OK);
    }
    // 뉴스레터 구독 취소 (사용자)
    @DeleteMapping("/{user_name}")
    public ResponseEntity<String> unsubscribeNews(@PathVariable("user_name") String userName) {
        return new ResponseEntity<>(subscribeService.unsubscribeNewsLetter(userName), HttpStatus.OK);
    }
    // 뉴스레터 구독 상태 수정 (사용자)
    @PutMapping("/{user_name}")
    public ResponseEntity<SubscribeDto> updateSubscribeNews(@PathVariable("user_name") String userName, @RequestBody SubscribeDto request) {
        return new ResponseEntity<>(subscribeService.updateSubscribe(userName, request), HttpStatus.OK);
    }

    // 뉴스레터 전송 (테스트용 uri)
    @PostMapping("/sendMail")
    @ResponseBody
    public String SendMail(@RequestParam String email) throws Exception {
        return subscribeService.sendNewsLetter(email);
    }

}
