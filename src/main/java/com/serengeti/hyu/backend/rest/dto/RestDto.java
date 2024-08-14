package com.serengeti.hyu.backend.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestDto {
    private String restName;
    private String description;
    private String category; // 카테고리 어케하지..?
    private String image;
    private String link;
    private String place; // 장소추가해야될듯
}
