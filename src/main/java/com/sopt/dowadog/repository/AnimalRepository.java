package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.model.domain.PublicAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal,Integer>, PagingAndSortingRepository<Animal,Integer>,JpaSpecificationExecutor<Animal> {

    @Query("select u from #{#entityName} u where u.noticeEddt > ?1 AND u.processState = ?2 order by u.noticeEddt ASC")
    Page<Animal> findAllBy(LocalDate localDate, String processState ,Pageable pageable);


    Optional<List<Animal>> findByCare(Care care);





    //Page<Animal>
//AND u.process_state NOT IN (?2)
    //,String processState

}
