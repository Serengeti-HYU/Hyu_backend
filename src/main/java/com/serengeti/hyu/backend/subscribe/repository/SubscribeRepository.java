package com.serengeti.hyu.backend.subscribe.repository;

import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findByDayOfWeek(DayOfWeek dayOfWeek);
}
