package com.serengeti.hyu.backend.rest.repository;

import com.serengeti.hyu.backend.rest.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestRepository extends JpaRepository<Rest, Integer> {
    boolean existsByRestName(String restName);
}
