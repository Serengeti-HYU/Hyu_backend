package com.serengeti.hyu.backend.subscribe.repository;

import com.serengeti.hyu.backend.subscribe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

}
