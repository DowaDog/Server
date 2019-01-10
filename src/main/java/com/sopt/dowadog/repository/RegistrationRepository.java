package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    Optional<List<Registration>> findByUserAndUserCheck(User user, boolean isComplete);
    boolean existsByUserAndRegStatusNotInAndValidReg(User user, String regStatus, boolean validReg);
    Optional<Registration> findFirstByUserOrderByIdDesc(User user);
    Optional<List<Registration>> findByAnimal(Animal animal);
}
