package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommunityRepository extends JpaRepository<Community, Integer>, PagingAndSortingRepository<Community, Integer> {

}
