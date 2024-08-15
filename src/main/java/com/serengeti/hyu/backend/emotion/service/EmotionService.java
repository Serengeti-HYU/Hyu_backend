package com.serengeti.hyu.backend.emotion.service;

import com.serengeti.hyu.backend.emotion.dto.EmotionDto;
import com.serengeti.hyu.backend.emotion.entity.Emotion;
import com.serengeti.hyu.backend.emotion.exception.EmotionAlreadyExistsException;
import com.serengeti.hyu.backend.emotion.repository.EmotionRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    // 특정 날짜를 기준으로 그 주의 감정 기록을 조회하는 메서드 추가
    public List<Emotion> getEmotionsByWeek(Long userId, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // 주의 첫날을 월요일로 명시적으로 설정
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        // 해당 주의 시작일 (월요일)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = cal.getTime();

        // 해당 주의 종료일 (일요일)
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = cal.getTime();

        return emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate);
    }

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
