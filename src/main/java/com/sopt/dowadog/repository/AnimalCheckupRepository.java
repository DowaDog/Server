package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.AnimalCheckup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalCheckupRepository extends JpaRepository<AnimalCheckup, Integer> {

    List<AnimalCheckup> findByAnimalUserAdoptId(int animalUserAdoptId);
    void deleteByAnimalUserAdoptId(int animalUserAdoptId);
    Optional<AnimalCheckup> findByAnimalUserAdoptIdAndInoculation(int animalUserAdopId, String inoculation);
}
