package com.serengeti.hyu.backend.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestDto {

    private int restId;

    private String restName;
    private String description;
    private String category;
    private String image;
    private String link;
    private String place; // 장소필드 추가
}
