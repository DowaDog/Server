package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.UserCardnewsScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCardnewsScrapRepository extends JpaRepository<UserCardnewsScrap, Integer> {
    List<UserCardnewsScrap> findByUser_IdAndCardnews_Id(final String userId, final int cardnewsId);
    void deleteByUser_IdAndCardnews_Id(final String userId, final int cardnewsId);

    //내 스크렙 소팅하기(크리에이티드 순으로)
    @Query("SELECT u FROM #{#entityName} u WHERE u.user.id=?1 GROUP BY u.cardnews.id ORDER BY u.createdAt DESC")
    List<UserCardnewsScrap> findAllBy(final String userId);


}
