package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Mailbox;
import com.sopt.dowadog.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MailboxRepository extends JpaRepository<Mailbox, Integer>, PagingAndSortingRepository<Mailbox, Integer> {

    List<Mailbox> findByUserOrderByCreatedAtDesc(User user);


}
