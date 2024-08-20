package com.serengeti.hyu.backend.character.dto;

import com.serengeti.hyu.backend.character.enums.Question;
import com.serengeti.hyu.backend.character.enums.ResponseOption;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import java.util.Map;

public class CharacterRequestDto {

    private Long userId;

    private Map<Question, ResponseOption> responses;

    public CharacterRequestDto() {}

    public CharacterRequestDto(Long userId, Map<Question, ResponseOption> responses) {
        this.userId = userId;
        this.responses = responses;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Question, ResponseOption> getResponses() {
        return responses;
    }

    public void setResponses(Map<Question, ResponseOption> responses) {
        this.responses = responses;
    }
}
