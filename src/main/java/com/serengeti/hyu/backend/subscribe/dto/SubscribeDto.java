package com.serengeti.hyu.backend.subscribe.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {

    private String email; // 구독 메일

    private DayOfWeek dayOfWeek;
}