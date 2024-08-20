package com.serengeti.hyu.backend.emotion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EmotionResponseDto {
    private String username;
    private String emotionImg;
    private String recordDate;
}
