package com.serengeti.hyu.backend.character.repository;

import com.serengeti.hyu.backend.character.entity.Character;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByUser(User user);
}