package com.serengeti.hyu.backend.rest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Rest")
public class Rest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restId;

    private String restName;
    private String description;
    private String category; // 카테고리 어케하지..?
    private String image;

    @Column(length = 1000)
    private String link;
    private String place; // 장소추가해야될듯
}