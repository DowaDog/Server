package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Cardnews;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardnewsRepository extends JpaRepository<Cardnews, Integer>, PagingAndSortingRepository<Cardnews,Integer> {
    List<Cardnews> findByTypeOrderByCreatedAtDesc(String type);
    List<Cardnews> findByTypeOrderByCreatedAtDesc(String type, Pageable pagable);
}
