package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Care;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@Repository
public interface CareRepository extends JpaRepository<Care, Integer> {
}
