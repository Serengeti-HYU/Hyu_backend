package com.serengeti.hyu.backend.character.entity;

import com.serengeti.hyu.backend.character.enums.Question;
import com.serengeti.hyu.backend.character.enums.ResponseOption;
import com.serengeti.hyu.backend.character.enums.ResultType;
import com.serengeti.hyu.backend.user.entity.User;
import jakarta.persistence.*;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Entity(name="user_character")
@Getter
@Setter
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User 엔티티와의 1:1 연관 관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자 엔티티

    // 사용자가 응답한 문항과 그에 대한 답변을 저장
    @ElementCollection
    @CollectionTable(name = "character_responses", joinColumns = @JoinColumn(name = "character_id"))
    @MapKeyColumn(name = "question")
    @Column(name = "answer")
    @Enumerated(EnumType.STRING)
    private Map<Question, ResponseOption> responses;

    @Enumerated(EnumType.STRING)
    private ResultType resultType; // 계산된 결과 유형

    // 기본 생성자
    public Character() {}

    // 매개변수가 있는 생성자
    public Character(User user, Map<Question, ResponseOption> responses) {
        this.user = user;
        this.responses = responses;
    }
}
