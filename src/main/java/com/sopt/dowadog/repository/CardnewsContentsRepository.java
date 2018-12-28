package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.CardnewsContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardnewsContentsRepository extends JpaRepository<CardnewsContents, Integer> {

    List<CardnewsContents> findByCardnewsId(int cardnewsId);
}
