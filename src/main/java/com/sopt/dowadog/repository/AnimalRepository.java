package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal,Integer>, PagingAndSortingRepository<Animal,Integer> {

    Page<Animal> findAllByOrderByNoticeEddtAsc(Pageable pageable);

}
