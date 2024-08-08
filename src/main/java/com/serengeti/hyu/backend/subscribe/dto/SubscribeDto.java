package com.serengeti.hyu.backend.subscribe.dto;

import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import lombok.*;

import java.time.DayOfWeek;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {

    private String email; // 구독 메일

    private DayOfWeek dayOfWeek;

    private Subscribe.Status status; // 구독 상태
}
