package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.AnimalStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalStoryRepository extends JpaRepository<AnimalStory,Integer> {

    List<AnimalStory> findAllByAnimal_Id(int animalId);
}
