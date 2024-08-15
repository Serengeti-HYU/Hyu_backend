package com.serengeti.hyu.backend.emotion.repository;

import com.serengeti.hyu.backend.emotion.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Integer> {
    @Query("SELECT e FROM Emotion e WHERE e.user.userId = :userId AND e.recordDate BETWEEN :startDate AND :endDate")
    List<Emotion> findByUser_UserIdAndRecordDateBetween(Long userId, Date startDate, Date endDate);
}
