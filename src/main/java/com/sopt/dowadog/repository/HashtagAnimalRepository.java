package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Hashtag;
import com.sopt.dowadog.model.domain.HashtagAnimal;
import com.sopt.dowadog.model.domain.Mailbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HashtagAnimalRepository extends JpaRepository<HashtagAnimal, Integer>, PagingAndSortingRepository<HashtagAnimal, Integer> {
    Page<HashtagAnimal> findAllByHashtag_Keyword(final String keyword,Pageable pageable);
}
