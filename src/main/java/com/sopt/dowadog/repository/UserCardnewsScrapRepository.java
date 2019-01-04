package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.UserCardnewsScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardnewsScrapRepository extends JpaRepository<UserCardnewsScrap, Integer> {
    List<UserCardnewsScrap> findByUser_IdAndCardnews_Id(final String userId, final int cardnewsId);
    void deleteByUser_IdAndCardnews_Id(final String userId, final int cardnewsId);
}
