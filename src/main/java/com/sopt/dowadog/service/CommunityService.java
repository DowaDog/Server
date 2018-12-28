package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    @Autowired
    CommunityRepository communityRepository;

    public DefaultRes<Community> createCommunityService(Community community){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, communityRepository.save(community));
    }

    public DefaultRes<List<Community>> readAllCommunityList(){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, communityRepository.findAll());
    }
}
