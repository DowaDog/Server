package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {
}
