package com.serengeti.hyu.backend.emoji.entity;

import com.serengeti.hyu.backend.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
