package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    User findByIdAndPassword(String id, String password);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    User findFirstByName(String username);

    @Query("select u from #{#entityName} u where u.deviceToken IS NOT NULL")
    List<User> findAllByNotDeviceToken ();
}
