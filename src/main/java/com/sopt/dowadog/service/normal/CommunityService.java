package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityImg;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.CommunityDto;
import com.sopt.dowadog.model.dto.CommunityListDto;
import com.sopt.dowadog.repository.CommunityImgRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.service.common.FileService;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    @Transactional
    public DefaultRes<Community> createCommunity(User user, Community community) {

        System.out.print(111111);
        List<MultipartFile> communityImgFileList = community.getCommunityImgFiles();// 멀티파트로 받기 사진 리스트

        List<CommunityImg> communityImgList = new ArrayList();

        if (Optional.ofNullable(communityImgFileList).isPresent()) {
            //todo 파일 검증 => 파일이 없으면 디폴트 이미지 or 아예 안넣든지 해야함
            //todo 파일이 null이냐도 체크하고 bytesize도 한번더!
            for (MultipartFile imgFile : communityImgFileList) {

                String filePath = S3Util.getFilePath(baseDir, imgFile);

                fileService.fileUpload(imgFile, filePath); // s3 upload

                System.out.print(imgFile.toString());
                System.out.print(filePath);

                CommunityImg communityImg = CommunityImg.builder()
                        .community(community)
                        .filePath(filePath)
                        .originFileName(imgFile.getOriginalFilename())
                        .build();


                communityImgList.add(communityImgRepository.save(communityImg));
            }
            community.setCommunityImgList(communityImgList);
        }else{
            community.setCommunityImgList(communityImgList);
        }
        community.setUser(user);

        System.out.print(community);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_COMMUNITY, communityRepository.save(community));
    }

    public DefaultRes<CommunityListDto> readCommunityList(User user, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "createdAt");


        // 레파지토리에서 페이지 객체로 가져옴
        Page<Community> communityPage = communityRepository.findAll(pageable);
        Pageable paging = communityPage.getPageable();

        //페이지 객체로부터 리스트 추출
        List<Community> communityList = communityPage.getContent();

        //페이지 DTO 리스트 객체 생성
        List<CommunityDto> communityDtoList = new ArrayList<>();

        //레파지토리 객체값들 복사
        for (Community community : communityList) {
            CommunityDto communityDto = community.getCommunityDto();
//            communityDto.setUserProfileImg(new StringBuilder()s3Endpoint);
            List<CommunityImg> communityImgs = new ArrayList<>();

            for(CommunityImg communityImg : community.getCommunityImgList()){
                CommunityImg imgTemp = new CommunityImg();

                imgTemp.setCreatedAt(communityImg.getCreatedAt());
                imgTemp.setUpdatedAt(communityImg.getUpdatedAt());
                imgTemp.setFilePath(S3Util.getImgPath(s3Endpoint,communityImg.getFilePath()));
                imgTemp.setOriginFileName(communityImg.getOriginFileName());
                imgTemp.setId(communityImg.getId());

                communityImgs.add(imgTemp);
            }

            communityDto.setCommunityImgList(communityImgs);

            communityDto = setCommunityDtoAuthAndProfileImgWithUser(user, community, communityDto);

            communityDtoList.add(communityDto);

        }

        //리턴할 데이터 빌더
        CommunityListDto communityListDto = CommunityListDto.builder()
                .pageable(paging)
                .content(communityDtoList)
                .build();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityListDto);
    }

    public DefaultRes<CommunityDto> readCommunityById(User user, int communityId) {

        String tempImg ="유저가 아님";
        if(user != null){
            tempImg =  user.getProfileImg();

        }

        if (communityRepository.findById(communityId).isPresent()) {
            Community community = communityRepository.findById(communityId).get();
            CommunityDto communityDto = community.getCommunityDto();
            communityDto.setUserProfileImg(S3Util.getImgPath(s3Endpoint,community.getUser().getProfileImg()));
            communityDto.setPresentUserImg(S3Util.getImgPath(s3Endpoint,tempImg));
            //커뮤니티 이미지들 풀패스로 바꾸는 과정
            List<CommunityImg> imgList = new ArrayList<>();
            for(CommunityImg temp : community.getCommunityImgList()){
                CommunityImg imgTemp = new CommunityImg();
                imgTemp.setCreatedAt(temp.getCreatedAt());
                imgTemp.setUpdatedAt(temp.getUpdatedAt());
                imgTemp.setFilePath(S3Util.getImgPath(s3Endpoint,temp.getFilePath()));
                imgTemp.setOriginFileName(temp.getOriginFileName());
                imgTemp.setId(temp.getId());
                imgList.add(imgTemp);
            }
            communityDto.setCommunityImgList(imgList);
            communityDto = setCommunityDtoAuthAndProfileImgWithUser(user, community, communityDto);


            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityDto);
        } else {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMUNITY);
        }


    }

    public DefaultRes<Community> updateCommunityById(User user, Community modifiedCommunity, int communityId) {

        if (!communityRepository.findById(communityId).isPresent()) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMUNITY);
        }
        Community community = communityRepository.findById(communityId).get();


        List<MultipartFile> communityImgFileList = modifiedCommunity.getCommunityImgFiles();
        List<CommunityImg> communityImgList = new ArrayList();
        System.out.println("## UPDATE COMMUNITY IMG COME ##");


        if (Optional.ofNullable(communityImgFileList).isPresent()) {

            System.out.println("here come img not null");
            //todo 파일 검증 => 파일이 없으면 디폴트 이미지 or 아예 안넣든지 해야함
            //todo 파일이 null이냐도 체크하고 bytesize도 한번더!
            for (MultipartFile imgFile : communityImgFileList) {

                String filePath = S3Util.getFilePath(baseDir, imgFile);

                fileService.fileUpload(imgFile, filePath); // s3 upload

                System.out.print(imgFile.toString());
                System.out.print(filePath);

                CommunityImg communityImg = CommunityImg.builder()
                        .community(community)
                        .filePath(filePath)
                        .originFileName(imgFile.getOriginalFilename())
                        .build();


                communityImgList.add(communityImgRepository.save(communityImg));
            }
            community.setCommunityImgList(communityImgList);
        }else{
            community.setCommunityImgList(communityImgList);
        }


        //이미지 삭제내용
        if (checkAuth(user.getId(), community.getUser().getId())) {
            community.setTitle(modifiedCommunity.getTitle());
            community.setDetail(modifiedCommunity.getDetail());

            System.out.println("title, detail 수정 완료");

            if(modifiedCommunity.getRemoveImgArray().length != 0 ){

                for(int i =0;i<modifiedCommunity.getRemoveImgArray().length ; i++){
                    System.out.println(modifiedCommunity.getRemoveImgArray()[i]);
                    communityImgRepository.deleteById(modifiedCommunity.getRemoveImgArray()[i]);
                    System.out.println("인덱스의 이미지 제거 완료");

                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_COMMUNITY, communityRepository.save(community));
        }

        return DefaultRes.UNAUTHORIZATION;
    }

    public DefaultRes<CommunityImg> deleteCommunityImgById(int communityImgId) {
        communityImgRepository.deleteById(communityImgId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMUNITYIMG);
    }

    public DefaultRes deleteCommunityById(User user, int communityId) {

        if (!communityRepository.findById(communityId).isPresent()) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMUNITY);
        }

        Community community = communityRepository.findById(communityId).get();


        if (checkAuth(user.getId(), community.getUser().getId())) {
            communityRepository.deleteById(communityId);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMUNITY);
        }
        return DefaultRes.UNAUTHORIZATION;
    }

    private boolean checkAuth(final String loginUserId, final String userId) {
        return loginUserId.equals(userId);
    }


    public CommunityDto setCommunityDtoAuthAndProfileImgWithUser(User user, Community community, CommunityDto communityDto) {
        if (user != null) communityDto.setAuth(community.getAuth(user.getId()));
        //todo 만약 디폴트 이미지가 있으면 유저 사진이 널일때랑 아닐때 분기해서 처리
        communityDto.setUserProfileImg(S3Util.getImgPath(s3Endpoint, community.getUser().getProfileImg()));

        return communityDto;
    }

    public Optional<List<Community>> readMyCommunity(User user) {



        return communityRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
