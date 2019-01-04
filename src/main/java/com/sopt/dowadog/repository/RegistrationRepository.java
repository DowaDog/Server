package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    boolean existsByUserAndRegStatusNotAndValidReg(User user, String regStatus, boolean validReg);
}
