package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.MailboxReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MailboxReservationRepository extends JpaRepository<MailboxReservation,Integer> {

    List<MailboxReservation> findAllByStateEqualsAndTimeEquals(final String state, final LocalDate localDate);
}
