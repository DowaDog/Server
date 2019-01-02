package com.sopt.dowadog.service;

import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.MyinfoDto;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.repository.AnimalUserAdoptRepository;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;

import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MailboxRepository mailboxRepository;
    @Autowired
    AnimalUserAdoptRepository animalUserAdoptRepository;
    @Autowired
    FileService fileService;

    @Value("${uploadpath.myinfo}")
    private String baseDir;

    //todo 우체통 API작성 controller 작성하기 테이블도 구성되야함

    //UserID로 정보가져오기
    public DefaultRes<MyinfoDto> readMypage(User user) {

        System.out.println(user.getName());
        System.out.println(user.isNewMailbox());
        System.out.println(user.getMyinfoDto().toString());
        MyinfoDto myinfoDto = user.getMyinfoDto();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MYINFO, myinfoDto);
    }

    //사용자 정보 수정
    public DefaultRes updateUserInfo(User user, User modifiedUser){

        if(Optional.ofNullable(modifiedUser.getProfileImgFile()).isPresent()) {
            String filePath = S3Util.getFilePath(baseDir, modifiedUser.getProfileImgFile());

            fileService.fileUpload(modifiedUser.getProfileImgFile(), filePath);
            user.setProfileImg(filePath);
        }

        user.setName(modifiedUser.getName());
        user.setPhone(modifiedUser.getPhone());
        user.setEmail(modifiedUser.getEmail());
        user.setBirth(modifiedUser.getBirth());

        userRepository.save(user);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_MYINFO);
    }

    //입양한 동물 리스트 //todo 리스폰스메세지 변경
    public DefaultRes<List<AnimalUserAdopt>> readAnimalUserAdoptList(User user) {
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, user.getAnimalUserAdoptList());
    }

    //입양한 동물 정보 조회
    public DefaultRes<AnimalUserAdopt> readAnimalUserAdoptById(User user, int adoptAnimalId) {
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalUserAdoptRepository.findById(adoptAnimalId));
    }

    //입양한 동물 정보 수정 //todo 여기 모델이 DTO로 받고, 예방접종내용까지 같이 줘야됨
    public DefaultRes<AnimalUserAdopt> updateAnimalByAnimalId(User user, AnimalUserAdopt modifiedAnimalUserAdopt, int adoptAnimalId) {

        AnimalUserAdopt animalUserAdopt = animalUserAdoptRepository.findById(adoptAnimalId);
        boolean auth = animalUserAdopt.getAuth(user.getId());

        if(auth) {
            animalUserAdopt.setName(modifiedAnimalUserAdopt.getName());
            animalUserAdopt.setGender(modifiedAnimalUserAdopt.getGender());
            animalUserAdopt.setKind(modifiedAnimalUserAdopt.getKind());
            animalUserAdopt.setBirth(modifiedAnimalUserAdopt.getBirth());
            animalUserAdopt.setWeight(modifiedAnimalUserAdopt.getWeight());
            animalUserAdopt.setNeuterYn(modifiedAnimalUserAdopt.isNeuterYn());
            animalUserAdoptRepository.save(animalUserAdopt);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER_ANIMAL, animalUserAdoptRepository.save(animalUserAdopt));
        } else {
            return DefaultRes.UNAUTHORIZATION;
        }
    }



    //사용자 좋아요 리스트 조회
    public DefaultRes<List<UserAnimalLike>> readLikeListByUserId(int page, int limit, String userId){
        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_LIKE, user.getUserAnimalLikeList());
    }

    //사용자 스크랩 리스트 조회
    public DefaultRes<List<UserCardnewsScrap>> readClipsListByUserId(int page, int limit, String userId){
        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP, user.getUserCardnewsScrapList());
    }
    //사용자 작성한 글 리스트 조회
    public DefaultRes<List<Community>> readCommunityListByUserId(int page, int limit, String userId){
        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_COMMUNITY, user.getCommunityList());
    }

    //우체통 리스트 조회
    public DefaultRes<List<Mailbox>> readMailboxesUserId(User user){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAILBOX, user.getMailboxList());
    }


}






