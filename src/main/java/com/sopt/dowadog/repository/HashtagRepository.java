package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
}
