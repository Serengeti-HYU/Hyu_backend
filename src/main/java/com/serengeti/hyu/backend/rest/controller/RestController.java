package com.serengeti.hyu.backend.rest.controller;

import com.serengeti.hyu.backend.rest.dto.RestDto;
import com.serengeti.hyu.backend.rest.entity.Rest;

import com.serengeti.hyu.backend.rest.service.RestService;
import com.serengeti.hyu.backend.rest.service.ScrapService;

import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import com.serengeti.hyu.backend.rest.repository.RestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/hue-activity")
public class RestController {

    @Autowired
    private RestService restService;

    @Autowired
    private RestRepository restRepository;


    @Autowired
    private ScrapService scrapService;

    @Autowired
    private UserRepository userRepository;

    private String deployUrl = "http://3.36.241.149:8080"; // 배포 주소 적용

    // 쉼활동 목록 조회 -> 성격유형검사의 여부에 따라서 결정됨
    @GetMapping("/recommend")
    public ResponseEntity<List<RestDto>> getCulturalEvents(
            @RequestParam(value = "start_index", defaultValue = "1") Integer startIndex,
            @RequestParam(value = "end_index", defaultValue = "80") Integer endIndex,
            @AuthenticationPrincipal User user) {

        try {
            restService.fetchAndSaveCulturalEventInfo(startIndex, endIndex, null, null, null);


            List<RestDto> response = restService.getRestData(user.getUserId());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }
    }


    // 쉼활동 상세 조회
    @GetMapping("/{restId}")
    public ResponseEntity<Rest> getRestById(@PathVariable int restId) {
        Rest rest = restService.getRestById(restId);
        if (rest != null) {
            return ResponseEntity.ok(rest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 쉼활동 저장
    @PostMapping("/{restId}/bookmark")
    public ResponseEntity<?> scrapRest(
            @PathVariable int restId,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자가 아닙니다.");
        }

        Rest rest = restService.getRestById(restId);
        if (rest == null) {
            return ResponseEntity.notFound().build();
        }

        scrapService.addScrap(user, rest);
        return ResponseEntity.ok("쉼활동 저장이 완료되었습니다.");
    }

    // 쉼활동 취소
    @DeleteMapping("/{restId}/bookmark")
    public ResponseEntity<?> removeScrap(
            @PathVariable int restId,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증된 사용자가 아닙니다.");
        }
        Rest rest = restService.getRestById(restId);
        if (rest == null) {
            return ResponseEntity.notFound().build();
        }

        scrapService.removeScrap(user, rest);
        return ResponseEntity.ok("스크랩이 취소되었습니다.");
    }

    // 쉼활동 공유링크 생성
    @GetMapping("/{restId}/share")
    public ResponseEntity<String> shareLink(@PathVariable int restId) {
        Rest rest = restService.getRestById(restId);
        if (rest == null) {
            return ResponseEntity.notFound().build();
        }

        // 배포 주소 적용
        String shareUrl = deployUrl + "/hue-activity/" + restId;
        return ResponseEntity.ok(shareUrl);
    }
}
