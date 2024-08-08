package com.serengeti.hyu.backend.news.repository;

import com.serengeti.hyu.backend.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

}
