package com.serengeti.hyu.backend.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDate date; // 뉴스레터 발송 요일

    private String link;
//    private String creator;
}
