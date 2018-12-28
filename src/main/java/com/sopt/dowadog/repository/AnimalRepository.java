package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal,Integer> {

    List<Animal> findAllByOrderByNoticeEddtDesc();

}
