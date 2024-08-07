package com.serengeti.hyu.backend.news.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {

    private String title;

    private String content;

    private LocalDate createdDate;

    private String link;
}