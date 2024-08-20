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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();

        List<Emotion> emotionsToday = emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate);
        if (!emotionsToday.isEmpty()) {
            throw new EmotionAlreadyExistsException("Emotion record already exists for today");
        }

        Emotion emotion = Emotion.builder()
                .user(user)
                .emotionImg(request.getEmotionImg()) // "/images/happy.png" 형태의 경로 저장
                .content(request.getContent())
                .recordDate(new Date())
                .build();

        return emotionRepository.save(emotion);
    }

    @Transactional
    public Emotion updateEmotion(Long userId, Date recordDate, EmotionDto request) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(recordDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = cal.getTime();

        Emotion existingEmotion = emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Emotion record not found"));

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
       //추가
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = cal.getTime();

        List<Emotion> emotions = emotionRepository.findByUser_UserIdAndRecordDateBetween(userId, startDate, endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return emotions.stream()
                .map(e -> EmotionResponseDto.builder()
                        .username(e.getUser().getUsername())
                        .emotionImg(e.getEmotionImg())
                        .recordDate(dateFormat.format(e.getRecordDate()))
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    public Emotion getEmotionByDate(Long userId, Date date) {
        //추가
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
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
