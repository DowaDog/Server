package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.AnimalUserAdopt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalUserAdoptRepository extends JpaRepository<AnimalUserAdopt,Integer> {
    Optional<AnimalUserAdopt> findById(int animalUserAdopId);
}
