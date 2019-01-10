package com.sopt.dowadog.repository;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityImgRepository extends JpaRepository<CommunityImg, Integer> {

    void deleteByCommunity(final Community community);
}
