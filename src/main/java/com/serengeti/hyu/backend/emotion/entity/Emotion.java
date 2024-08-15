package com.serengeti.hyu.backend.emotion.entity;

import com.serengeti.hyu.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
@Table(name="Emotion")
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false) // 외래 키 열 이름을 명시적으로 SnakeCase로 지정
    private User user;

    private String emotionImg;
    private Date recordDate;
    private String content;
}
