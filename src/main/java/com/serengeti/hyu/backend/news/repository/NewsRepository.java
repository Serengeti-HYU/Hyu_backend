package com.serengeti.hyu.backend.news.repository;

import com.serengeti.hyu.backend.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByDate(LocalDate date);

}
