package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    boolean existsByUserAndRegStatusNotInAndValidReg(User user, String regStatus, boolean validReg);
    Optional<List<Registration>> findByUserAndUserCheck(User user, boolean userCheck);

}
