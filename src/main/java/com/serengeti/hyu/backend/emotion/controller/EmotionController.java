package com.serengeti.hyu.backend.emotion.controller;

import com.serengeti.hyu.backend.emotion.dto.EmotionDetailResponseDto;
import com.serengeti.hyu.backend.emotion.dto.EmotionDto;
import com.serengeti.hyu.backend.emotion.dto.EmotionResponseDto;
import com.serengeti.hyu.backend.emotion.entity.Emotion;
import com.serengeti.hyu.backend.emotion.exception.EmotionAlreadyExistsException;
import com.serengeti.hyu.backend.emotion.service.EmotionService;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hue-records")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createEmotion(@RequestBody EmotionDto request, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }
        Long userId = user.getUserId();
        emotionService.createEmotion(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Emotion record created successfully");
    }

    @PatchMapping("/{recordDate}")
    public ResponseEntity<EmotionDetailResponseDto> updateEmotion(@PathVariable String recordDate, @RequestBody EmotionDto request, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long userId = user.getUserId();

        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(recordDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 날짜 포맷 처리
        }

        Emotion emotion = emotionService.updateEmotion(userId, date, request);

        EmotionDetailResponseDto responseDto = EmotionDetailResponseDto.builder()
                .username(emotion.getUser().getUsername())
                .content(emotion.getContent())
                .emotionImg(emotion.getEmotionImg())
                .recordDate(emotion.getRecordDate().toString())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<EmotionResponseDto>> getEmotions(@RequestParam(required = false) String date, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long userId = user.getUserId();
        Date queryDate;
        if (date == null) {
            queryDate = new Date();
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                queryDate = dateFormat.parse(date);
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        List<EmotionResponseDto> emotions = emotionService.getEmotionsByWeek(userId, queryDate);
        return ResponseEntity.ok(emotions);
    }

    @GetMapping("/{recordDate}")
    public ResponseEntity<EmotionDetailResponseDto> getEmotionByDate(@PathVariable String recordDate, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Long userId = user.getUserId();
        Date queryDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // 엄격한 날짜 파싱
            queryDate = dateFormat.parse(recordDate);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }

        Emotion emotion = emotionService.getEmotionByDate(userId, queryDate);
        if (emotion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        EmotionDetailResponseDto responseDto = EmotionDetailResponseDto.builder()
                .username(emotion.getUser().getUsername())
                .content(emotion.getContent())
                .emotionImg(emotion.getEmotionImg())
                .recordDate(emotion.getRecordDate().toString())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(EmotionAlreadyExistsException.class)
    public ResponseEntity<String> handleEmotionAlreadyExistsException(EmotionAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}

