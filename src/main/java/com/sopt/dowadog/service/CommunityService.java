package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityImg;
import com.sopt.dowadog.repository.CommunityImgRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommunityService {

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityImgRepository communityImgRepository;

    @Autowired
    FileService fileService;

    @Value("${uploadpath.community}")
    private String baseDir;

    @Transactional
    public DefaultRes<Community> createCommunityService(Community community) throws Exception {

        List<MultipartFile> communityImgList = community.getCommunityImgFiles();

        for(MultipartFile imgFile : communityImgList) {

            String filePath = new StringBuilder(baseDir).
                                    append(S3Util.getUuid()).
                                    append(imgFile.getOriginalFilename()).toString();

            fileService.fileUpload(imgFile, filePath); // s3 upload

            CommunityImg communityImg = CommunityImg.builder()
                                            .community(community)
                                            .filePath(filePath)
                                            .originFileName(imgFile.getOriginalFilename())
                                            .build();

            communityImgRepository.save(communityImg);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_COMMUNITY, communityRepository.save(community));
    }

    public DefaultRes<Page<Community>> readCommunityList(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityRepository.findAll(pageable));
    }

    public DefaultRes<Community> readCommunityById(int communityId){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityRepository.findById(communityId).get());
    }

    public DefaultRes<Community> updateCommunityById(Community modifiedCommunity, int communityId){

        Community community = communityRepository.getOne(communityId);

        community.setTitle(modifiedCommunity.getTitle());
        community.setDetail(modifiedCommunity.getDetail());

        communityRepository.save(community);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_COMMUNITY, communityRepository.findById(communityId).get());
    }

    public DefaultRes<Community> deleteCommunityById(int communityId){

        communityRepository.deleteById(communityId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMUNITY);
    }


}
