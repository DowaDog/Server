package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.PublicAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublicAnimalRepository extends JpaRepository<PublicAnimal,Integer>, JpaSpecificationExecutor<PublicAnimal> {
}
