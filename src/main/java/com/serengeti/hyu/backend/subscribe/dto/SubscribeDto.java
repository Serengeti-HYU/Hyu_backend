package com.serengeti.hyu.backend.subscribe.dto;

import com.serengeti.hyu.backend.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeDto {
    private List<LocalDate> date; // 구독 요일

    private String email; // 구독 메일
}
