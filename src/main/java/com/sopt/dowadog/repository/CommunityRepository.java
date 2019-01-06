package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Integer>, PagingAndSortingRepository<Community, Integer> {



    Optional<List<Community>> findByUserOrderByCreatedAtDesc(User user);
}
