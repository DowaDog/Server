package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Integer> {
}
