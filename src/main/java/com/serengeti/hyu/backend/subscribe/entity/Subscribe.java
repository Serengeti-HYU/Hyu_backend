package com.serengeti.hyu.backend.subscribe.entity;

import com.serengeti.hyu.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user; // 구독 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek; // 구독 요일

    @Column(nullable = false)
    private String email; // 구독 메일

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status; // 구독 상태

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
