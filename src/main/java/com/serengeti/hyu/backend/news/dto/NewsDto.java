package com.serengeti.hyu.backend.news.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {

    private String title;

    private String content;

    private Date createdDate;

    private String link;
}
