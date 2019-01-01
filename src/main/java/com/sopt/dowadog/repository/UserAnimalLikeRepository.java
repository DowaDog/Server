package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.UserAnimalLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnimalLikeRepository extends JpaRepository<UserAnimalLike,Integer> {

    List<UserAnimalLike> findAllByUser_IdAndAnimal_Id(final String userIdx,final int animalIdx);

    void deleteByUser_idAndAnimal_Id(final String userIdx,final int animalIdx);
}
