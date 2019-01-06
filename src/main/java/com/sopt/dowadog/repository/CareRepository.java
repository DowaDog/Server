package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Care;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CareRepository extends JpaRepository<Care, Integer>, PagingAndSortingRepository<Care,Integer> {
    Optional<Care> findByCareUserId(String careUserId);
    Care findByCareUserIdAndPassword(String careUserId, String password);



}
