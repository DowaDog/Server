package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Hashtag;
import com.sopt.dowadog.model.domain.HashtagAnimal;
import com.sopt.dowadog.model.domain.Mailbox;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;

public interface HashtagAnimalRepository extends JpaRepository<HashtagAnimal, Integer>, PagingAndSortingRepository<HashtagAnimal, Integer> {

    @Query("select u from #{#entityName} u where u.hashtag.keyword=?1 AND u.animal.noticeEddt > ?2")
    Page<HashtagAnimal> findAllByHashtag_Keyword(final String keyword, final LocalDate localDate, Pageable pageable);
}
