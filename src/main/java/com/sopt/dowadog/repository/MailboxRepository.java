package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Mailbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MailboxRepository extends JpaRepository<Mailbox, Integer>, PagingAndSortingRepository<Mailbox, Integer> {
    Mailbox findByUserId(String userId);

}
