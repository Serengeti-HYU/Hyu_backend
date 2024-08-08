package com.serengeti.hyu.backend.subscribe.entity;

import com.serengeti.hyu.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    private Long sub_id;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user; // 구독 사용자

    @ElementCollection
    private List<LocalDate> date; // 구독 요일

    private String email; // 구독 메일
}
