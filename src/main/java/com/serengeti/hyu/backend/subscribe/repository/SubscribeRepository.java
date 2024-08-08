package com.serengeti.hyu.backend.subscribe.repository;

import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findByDayOfWeek(DayOfWeek dayOfWeek);

    // 사용자와 이메일로 구독 정보 조회
    Optional<Subscribe> findByUserAndEmail(User user, String email);

    Optional<Subscribe> findByUser(User user);

    boolean existsByUserAndEmail(User user, String email);
}
