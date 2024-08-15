package com.serengeti.hyu.backend.rest.repository;


import com.serengeti.hyu.backend.rest.entity.Rest;
import com.serengeti.hyu.backend.rest.entity.Scrap;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    List<Scrap> findByUser(User user);

    Scrap findByUserAndRest(User user, Rest rest);
}

