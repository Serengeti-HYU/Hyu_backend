package com.serengeti.hyu.backend.emotion.controller;

import com.serengeti.hyu.backend.emotion.dto.EmotionDto;
import com.serengeti.hyu.backend.emotion.entity.Emotion;
import com.serengeti.hyu.backend.emotion.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serengeti.hyu.backend.emotion.exception.EmotionAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hue-records")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    @PostMapping("/{userId}")
    public ResponseEntity<Emotion> createEmotion(@PathVariable Long userId, @RequestBody EmotionDto request) {
        Emotion emotion = emotionService.createEmotion(userId, request);
        return ResponseEntity.ok(emotion);
    }

//    @PostMapping("/{userId}")
//    public ResponseEntity<Emotion> createEmotion(
//            @PathVariable Long userId,
//            @RequestParam("emotionImg") MultipartFile file,
//            @RequestParam("content") String content
//    ) {
//        // 파일 저장 로직
//        String imageUrl = saveImage(file); // 파일을 서버에 저장하고 경로 반환
//
//        EmotionDto request = new EmotionDto();
//        request.setEmotionImg(imageUrl);
//        request.setContent(content);
//
//        Emotion emotion = emotionService.createEmotion(userId, request);
//        return ResponseEntity.ok(emotion);
//    }
//
//    public String saveImage(MultipartFile file) {
//        try {
//            // 파일 저장 디렉토리 설정
//            String directoryPath = "path/to/save/images/";
//            String fileName = file.getOriginalFilename();
//            File dest = new File(directoryPath + fileName);
//            file.transferTo(dest);
//            return directoryPath + fileName; // 이미지 경로 반환
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file", e);
//        }
//    }


    @PatchMapping("/{userId}/{recordId}")
    public ResponseEntity<Emotion> updateEmotion(@PathVariable Long userId, @PathVariable int recordId, @RequestBody EmotionDto request) {
        Emotion emotion = emotionService.updateEmotion(userId, recordId, request);
        return ResponseEntity.ok(emotion);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Emotion>> getEmotions(@PathVariable Long userId, @RequestParam(required = false) String date) {
        Date queryDate;
        if (date == null) {
            // 날짜가 지정되지 않은 경우 오늘 날짜로 설정
            queryDate = new Date();
        } else {
            // 지정된 날짜 파싱
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                queryDate = dateFormat.parse(date);
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body(null); // 잘못된 날짜 포맷 처리
            }
        }

        List<Emotion> emotions = emotionService.getEmotionsByWeek(userId, queryDate);
        return ResponseEntity.ok(emotions);
    }

    @GetMapping("/{userId}/detail")
    public ResponseEntity<Emotion> getEmotionByDate(@PathVariable Long userId, @RequestParam String date) {
        Date queryDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            queryDate = dateFormat.parse(date);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 날짜 포맷 처리
        }

        Emotion emotion = emotionService.getEmotionByDate(userId, queryDate);
        if (emotion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 감정 기록이 없을 경우 404 반환
        }

        return ResponseEntity.ok(emotion);
    }


    @ExceptionHandler(EmotionAlreadyExistsException.class)
    public ResponseEntity<String> handleEmotionAlreadyExistsException(EmotionAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
