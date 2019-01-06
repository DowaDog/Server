package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Integer> {
    List<CommunityComment> findByCommunityId(int communityId);
}
