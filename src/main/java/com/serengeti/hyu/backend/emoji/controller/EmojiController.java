package com.serengeti.hyu.backend.emoji.controller;

import com.serengeti.hyu.backend.emoji.dto.CreateEmojiDto;
import com.serengeti.hyu.backend.emoji.dto.EmojiDto;
import com.serengeti.hyu.backend.emoji.service.EmojiService;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hue-emoji")
public class EmojiController {

    @Autowired
    private EmojiService emojiService;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new IllegalStateException("User not authenticated or invalid principal type.");
    }

    @GetMapping
    public ResponseEntity<List<EmojiDto>> getUserEmojis() {
        User authenticatedUser = getAuthenticatedUser();

        List<EmojiDto> defaultEmojis = List.of(
                new EmojiDto(1L, "/static/images/grumpy.png"),
                new EmojiDto(2L, "/static/images/happy.png"),
                new EmojiDto(3L, "/static/images/sad.png"),
                new EmojiDto(4L, "/static/images/smile.png"),
                new EmojiDto(5L, "/static/images/tired.png")
        );

        // 유저가 생성한 표정
        List<EmojiDto> userEmojis = emojiService.getEmojisByUser(authenticatedUser);

        // 유저가 생성한 표정이 없으면 기본 표정만 반환
        if (userEmojis.isEmpty()) {
            return ResponseEntity.ok(defaultEmojis);
        }

        // 기본 표정과 유저 생성 표정을 병합
        List<EmojiDto> combinedEmojis = new ArrayList<>(defaultEmojis);
        combinedEmojis.addAll(userEmojis);

        return ResponseEntity.ok(combinedEmojis);
    }

    @PostMapping
    public ResponseEntity<EmojiDto> createEmoji(@Valid @RequestBody CreateEmojiDto createEmojiDto) {
        User authenticatedUser = getAuthenticatedUser();
        EmojiDto createdEmoji = emojiService.createEmoji(authenticatedUser, createEmojiDto);
        return ResponseEntity.ok(createdEmoji);
    }
}
