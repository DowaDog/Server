package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Hashtag;
import com.sopt.dowadog.model.domain.Mailbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer>, PagingAndSortingRepository<Hashtag, Integer> {
    Page<Hashtag> findAllByKeyword(final String tag, Pageable pageable);
}
