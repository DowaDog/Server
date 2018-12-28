package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Cardnews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardnewsRepository extends JpaRepository<Cardnews, Integer> {
    List<Cardnews> findByType(String type);
}
