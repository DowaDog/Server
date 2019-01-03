package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityImg;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.CommunityDto;
import com.sopt.dowadog.model.dto.CommunityListDto;
import com.sopt.dowadog.repository.CommunityImgRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public DefaultRes<Community> createCommunityService(User user, Community community) throws Exception {

        List<MultipartFile> communityImgFileList = community.getCommunityImgFiles();
        List<CommunityImg> communityImgList = new ArrayList();

        if(community.getCommunityImgFiles() != null) {
            for (MultipartFile imgFile : communityImgFileList) {

                String filePath = new StringBuilder(baseDir).
                        append(S3Util.getUuid()).
                        append(imgFile.getOriginalFilename()).toString();

                fileService.fileUpload(imgFile, filePath); // s3 upload

                CommunityImg communityImg = CommunityImg.builder()
                        .community(community)
                        .filePath(filePath)
                        .originFileName(imgFile.getOriginalFilename())
                        .build();

                communityImgList.add(communityImgRepository.save(communityImg));
            }
            community.setUser(user);
            community.setCommunityImgList(communityImgList);

        }



        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_COMMUNITY, communityRepository.save(community));
    }

    public DefaultRes<CommunityListDto> readCommunityList(User user, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "createdAt");

        // 레파지토리에서 페이지 객체로 가져옴
        Page<Community> communityPage = communityRepository.findAll(pageable);
        Pageable paging = communityPage.getPageable();

        //페이지 객체로부터 리스트 추출
        List<Community> communityList = communityPage.getContent();

        //페이지 DTO 리스트 객체 생성
        List<CommunityDto> communityDtoList = new ArrayList<>();

        //레파지토리 객체값들 복사
        for(Community community : communityList) {
            CommunityDto communityDto = community.getCommunityDto();
            communityDto.setAuth(community.getAuth(user.getId()));

            communityDtoList.add(communityDto);

        }

        //리턴할 데이터 빌더
        CommunityListDto communityListDto = CommunityListDto.builder()
                                                .pageable(paging)
                                                .content(communityDtoList)
                                                .build();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityListDto);
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

    public DefaultRes<CommunityImg> deleteCommunityImgById(int communityImgId){
        communityImgRepository.deleteById(communityImgId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMUNITYIMG);
    }

    public DefaultRes<Community> deleteCommunityById(int communityId){

        communityRepository.deleteById(communityId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMUNITY);
    }


}
