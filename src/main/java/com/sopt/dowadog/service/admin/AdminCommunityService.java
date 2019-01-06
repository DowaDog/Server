package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityImg;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.CommunityImgRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.common.FileService;
import com.sopt.dowadog.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;



/**
 * Created by kohen.kang on 2019-01-04..
 */
@Service
public class AdminCommunityService {

    @Value("${uploadpath.community}")
    private String baseDir;

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    @Autowired
    private FileService fileService;

    @Autowired
    CommunityImgRepository communityImgRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommunityRepository communityRepository;


    @Transactional
    public void createCommunity(String userId, Community community) {
        List<MultipartFile> communityImgFileList = community.getCommunityImgFiles();

        User user = userRepository.findById(userId).get();

        if (community.getCommunityImgFiles() != null) {


            for (MultipartFile imgFile : communityImgFileList) {

                if(imgFile.getSize() == 0) break; //이미지 파일 비어있으면 탈출

                String filePath = S3Util.getFilePath(baseDir, imgFile);

                fileService.fileUpload(imgFile, filePath); // s3 upload

                CommunityImg communityImg = CommunityImg.builder()
                        .community(community)
                        .filePath(filePath)
                        .originFileName(imgFile.getOriginalFilename())
                        .build();

                communityImgRepository.save(communityImg);
            }
        }
        community.setUser(user);



        communityRepository.save(community);
    }


}
