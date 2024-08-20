package com.serengeti.hyu.backend.character.dto;

import com.serengeti.hyu.backend.character.enums.ResultType;

public class CharacterResponseDto {

    private Long userId;
    private ResultType resultType;
    private String typeName;
    private String description;

    public CharacterResponseDto() {}

    public CharacterResponseDto(Long userId, ResultType resultType) {
        this.userId = userId;
        this.resultType = resultType;
        this.typeName = resultType.getTypeName();
        this.description = resultType.getDescription();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
        this.typeName = resultType.getTypeName();
        this.description = resultType.getDescription();
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }
}
