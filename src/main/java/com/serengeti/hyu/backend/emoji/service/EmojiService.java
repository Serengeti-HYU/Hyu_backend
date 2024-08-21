package com.serengeti.hyu.backend.emoji.service;

import com.serengeti.hyu.backend.emoji.dto.CreateEmojiDto;
import com.serengeti.hyu.backend.emoji.dto.EmojiDto;
import com.serengeti.hyu.backend.emoji.entity.Emoji;
import com.serengeti.hyu.backend.emoji.repository.EmojiRepository;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmojiService {

    @Autowired
    private EmojiRepository emojiRepository;

    public List<EmojiDto> getEmojisByUser(User user) {
        List<Emoji> emojis = emojiRepository.findByUser(user);
        return emojis.stream()
                .map(emoji -> {
                    EmojiDto dto = new EmojiDto();
                    dto.setId(emoji.getId());
                    dto.setImageUrl(emoji.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public EmojiDto createEmoji(User user, CreateEmojiDto createEmojiDto) {
        Emoji emoji = new Emoji();
        emoji.setImageUrl(createEmojiDto.getImageUrl());
        emoji.setUser(user);
        Emoji savedEmoji = emojiRepository.save(emoji);

        EmojiDto dto = new EmojiDto();
        dto.setId(savedEmoji.getId());
        dto.setImageUrl(savedEmoji.getImageUrl());
        return dto;
    }
}
