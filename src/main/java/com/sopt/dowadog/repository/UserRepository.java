package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
//    List<User> findAllByBirthIsNotLike();
}
