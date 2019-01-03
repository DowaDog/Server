package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.AnimalUserAdopt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalUserAdoptRepository extends JpaRepository<AnimalUserAdopt,Integer> {
    AnimalUserAdopt findById(int animalUserAdopId);
}
