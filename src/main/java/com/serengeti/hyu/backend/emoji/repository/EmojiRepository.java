package com.serengeti.hyu.backend.emoji.repository;

import com.serengeti.hyu.backend.emoji.entity.Emoji;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    List<Emoji> findByUser(User user);
}
