package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {

    List<Code> findByCodeGroup(String codeGroup);
}


