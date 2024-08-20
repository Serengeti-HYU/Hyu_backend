package com.serengeti.hyu.backend.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serengeti.hyu.backend.emotion.entity.Emotion;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private Date birth;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;

    @Column(name="personality_test", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE") // 데이터베이스의 컬럼 이름에 맞추어 설정
    private boolean isPersonalityTest = false; //

//    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
//    private boolean isPremium = false;


    @JsonIgnore //json 직렬화 되지 않게 함
    @OneToMany(mappedBy = "user")
    private List<Emotion> emotions;


//    @OneToMany(mappedBy = "user")
//    private List<Scrap> scraps;

//    @OneToMany(mappedBy = "user")
//    private List<Payment> payments;

//    @OneToMany(mappedBy = "user")
//    private List<Result> results;

}