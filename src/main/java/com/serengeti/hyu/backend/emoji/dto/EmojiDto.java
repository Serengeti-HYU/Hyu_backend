package com.serengeti.hyu.backend.emoji.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmojiDto {
    private Long id;
    private String imageUrl;
}
