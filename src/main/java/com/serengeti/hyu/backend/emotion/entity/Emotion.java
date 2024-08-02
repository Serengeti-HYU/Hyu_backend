package com.serengeti.hyu.backend.emotion.entity;


import com.serengeti.hyu.backend.user.entity.User;
import jakarta.persistence.*;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Emotion")
public class Emotion  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String emotionImg;
    private String emotion;
    private Date recordDate;
    private String content;
}
