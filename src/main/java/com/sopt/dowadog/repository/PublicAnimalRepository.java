package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.PublicAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface PublicAnimalRepository extends JpaRepository<PublicAnimal,Integer>, JpaSpecificationExecutor<PublicAnimal> {


    @Query("select u from #{#entityName} u where u.noticeEdt > ?1 order by u.noticeEdt ASC")
    Page<PublicAnimal> findAllBy(String noticeEdt, Pageable pageable);

}
