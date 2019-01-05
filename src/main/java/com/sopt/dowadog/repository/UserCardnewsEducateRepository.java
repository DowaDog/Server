package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.UserCardnewsEducate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardnewsEducateRepository extends JpaRepository<UserCardnewsEducate, Integer> {
    List<UserCardnewsEducate> findByUser_IdAndCardnews_Id(final String userId, final int cardnewsId);
    Optional<List<UserCardnewsEducate>> findAllByUser_Id(final String userId);
}
