package com.serengeti.hyu.backend.emotion.service;

import com.serengeti.hyu.backend.emotion.dto.EmotionDto;
import com.serengeti.hyu.backend.emotion.dto.EmotionResponseDto;
import com.serengeti.hyu.backend.emotion.entity.Emotion;
import com.serengeti.hyu.backend.emotion.exception.EmotionAlreadyExistsException;
import com.serengeti.hyu.backend.emotion.repository.EmotionRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmotionService {

    @Autowired
    private EmotionRepository emotionRepository;

    @Autowired
    private UserRepository userRepository;

    public Emotion createEmotion(Long userId, EmotionDto request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 오늘 날짜의 시작과 끝 시간 설정
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();

        // 오늘 날짜에 이미 감정 기록이 있는지 확인
        List<Emotion> emotionsToday = emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate);
        if (!emotionsToday.isEmpty()) {
            throw new EmotionAlreadyExistsException("Emotion record already exists for today");
        }

        Emotion emotion = Emotion.builder()
                .user(user)
                .emotionImg(request.getEmotionImg())
                .content(request.getContent())
                .recordDate(new Date())
                .build();

        return emotionRepository.save(emotion);
    }

    @Transactional
    public Emotion updateEmotion(Long userId, int recordId, EmotionDto request) {
        Emotion existingEmotion = emotionRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Emotion record not found"));

        if (!existingEmotion.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("User ID does not match");
        }

        Emotion updatedEmotion = Emotion.builder()
                .recordId(existingEmotion.getRecordId())
                .user(existingEmotion.getUser())
                .emotionImg(request.getEmotionImg() != null ? request.getEmotionImg() : existingEmotion.getEmotionImg())
                .content(request.getContent() != null ? request.getContent() : existingEmotion.getContent())
                .recordDate(existingEmotion.getRecordDate())
                .build();

        return emotionRepository.save(updatedEmotion);
    }


    @Transactional
    public List<EmotionResponseDto> getEmotionsByWeek(Long userId, Date date) {
        // 주의 첫날과 마지막 날을 계산
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // 해당 주의 시작일 (월요일)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = cal.getTime();

        // 해당 주의 종료일 (일요일)
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = cal.getTime();

        // Emotion 엔터티 리스트를 가져옴
        List<Emotion> emotions = emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate);

        // Emotion 엔터티 리스트를 EmotionResponseDto 리스트로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return emotions.stream()
                .map(emotion -> new EmotionResponseDto(
                        emotion.getUser().getUsername(),
                        emotion.getEmotionImg(),
                        dateFormat.format(emotion.getRecordDate())
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    //게시글 상세조회
    public Emotion getEmotionByDate(Long userId, Date recordDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(recordDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();

        return emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
