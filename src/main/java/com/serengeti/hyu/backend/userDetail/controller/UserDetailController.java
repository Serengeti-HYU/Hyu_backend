package com.serengeti.hyu.backend.userDetail.controller;

import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.service.CharacterService;
import com.serengeti.hyu.backend.emotion.dto.EmotionResponseDto;
import com.serengeti.hyu.backend.emotion.service.EmotionService;
import com.serengeti.hyu.backend.rest.entity.Scrap;
import com.serengeti.hyu.backend.rest.service.ScrapService;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class UserDetailController {

    private final UserRepository userRepository;
    private final CharacterService characterService;
    private final EmotionService emotionService;
    private final ScrapService scrapService;

    @Autowired
    public UserDetailController(UserRepository userRepository,
                                CharacterService characterService,
                                EmotionService emotionService,
                                ScrapService scrapService) {
        this.userRepository = userRepository;
        this.characterService = characterService;
        this.emotionService = emotionService;
        this.scrapService = scrapService;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new IllegalStateException("User not authenticated or invalid principal type.");
    }

    private int calculateAge(Date birthDate) {
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        return Period.between(birthDateLocal, today).getYears();
    }

    private Map<String, Integer> getBirthDateComponents(Date birthDate) {
        LocalDate birthDateLocal = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Map<String, Integer> birthDateComponents = new HashMap<>();
        birthDateComponents.put("birthyear", birthDateLocal.getYear());
        birthDateComponents.put("birthmonth", birthDateLocal.getMonthValue());
        birthDateComponents.put("birthday", birthDateLocal.getDayOfMonth());
        return birthDateComponents;
    }

    // 1. 마이페이지 조회
    @GetMapping("/{username}/")
    public ResponseEntity<Map<String, Object>> getUserDetails(@PathVariable String username) {
        User authenticatedUser = getAuthenticatedUser();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserId().equals(authenticatedUser.getUserId())) {
            return ResponseEntity.status(403).body(null);
        }

        CharacterResponseDto characterResponse = characterService.getCharacterResult(user.getUserId());

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", user.getName());
        userDetails.putAll(getBirthDateComponents(user.getBirth()));
        userDetails.put("age", calculateAge(user.getBirth()));
        userDetails.put("characterType", characterResponse.getTypeName());

        return ResponseEntity.ok(userDetails);
    }

    // 2. 최근 감정 기록 조회
    @GetMapping("/{username}/hue-records")
    public ResponseEntity<List<EmotionResponseDto>> getRecentEmotions(@PathVariable String username) {
        User authenticatedUser = getAuthenticatedUser();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserId().equals(authenticatedUser.getUserId())) {
            return ResponseEntity.status(403).body(null);
        }

        List<EmotionResponseDto> emotions = emotionService.getEmotionsByWeek(user.getUserId(), new Date());

        // 최근 3개의 감정 기록만 필터링
        List<EmotionResponseDto> recentEmotions = emotions.stream()
                .sorted(Comparator.comparing(EmotionResponseDto::getRecordDate).reversed())
                .limit(3)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recentEmotions);
    }

    // 3. 사용자 쉼 활동 저장 목록 조회
    @GetMapping("/{username}/hue-activity")
    public ResponseEntity<List<Scrap>> getUserScraps(@PathVariable String username) {
        User authenticatedUser = getAuthenticatedUser();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserId().equals(authenticatedUser.getUserId())) {
            return ResponseEntity.status(403).body(null);
        }

        List<Scrap> scraps = scrapService.getScrapByUser(user.getUserId());
        return ResponseEntity.ok(scraps);
    }
}
