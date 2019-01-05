package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.UserCardnewsEducate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardnewsEducateRepository extends JpaRepository<UserCardnewsEducate, Integer> {
    List<UserCardnewsEducate> findByUserIdAndCardnewsId(final String userId, final int cardnewsId);
}
