package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.*;
import com.sopt.dowadog.repository.AnimalCheckupRepository;
import com.sopt.dowadog.repository.AnimalUserAdoptRepository;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AnimalService animalService;
    @Autowired
    CommunityService communityService;
    @Autowired
    MailboxRepository mailboxRepository;
    @Autowired
    AnimalUserAdoptRepository animalUserAdoptRepository;
    @Autowired
    FileService fileService;
    @Autowired
    CodeService codeService;
    @Autowired
    AnimalCheckupRepository animalCheckupRepository;

    @Value("${uploadpath.myinfo}")
    private String baseDir;
    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    //todo 우체통 API작성 controller 작성하기 테이블도 구성되야함

    //UserID로 정보가져오기
    public DefaultRes<MyinfoDto> readMypage(User user) {

        MyinfoDto myinfoDto = user.getMyinfoDto();
        myinfoDto.setProfileImg(S3Util.getImgPath(s3Endpoint, user.getProfileImg()));

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MYINFO, myinfoDto);
    }

    //사용자 정보 수정
    public DefaultRes updateUserInfo(User user, User modifiedUser) {

        if (Optional.ofNullable(modifiedUser.getProfileImgFile()).isPresent()) {
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
    public DefaultRes<AdoptAnimalDto> readAnimalUserAdoptById(User user, int adoptAnimalId) {

        //adopAnimalId를 가진 동물 조회
        AdoptAnimalDto animalDto = AdoptAnimalDto.builder().animalUserAdopt(animalUserAdoptRepository.findById(adoptAnimalId)).build();

        //리턴하기위한 코드리스트 생성
        List<InoculationCode> codeDtoList = new ArrayList<>();

        //전체 예방접종 리스트
        List<Code> codeList = codeService.readCodeByCodeGroup("inoculation");

        //해당 입양동물의 체크업 리스트
        List<AnimalCheckup> animalCheckupList = animalCheckupRepository.findByAnimalUserAdoptId(adoptAnimalId);


        // codeDto 구성 비즈니스로직
        for (Code code : codeList) {
            InoculationCode inoculationCode = code.getCodeDto();
            for (AnimalCheckup animalCheckup : animalCheckupList) {
                if (code.getCode().equals(animalCheckup.getInoculation())) inoculationCode.setComplete(true);
            }
            codeDtoList.add(inoculationCode);
        }

        animalDto.setInoculationList(codeDtoList);


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalDto);
    }

    //입양한 동물 정보 수정 //todo 여기 모델이 DTO로 받고, 예방접종내용까지 같이 줘야됨
    @Transactional
    public DefaultRes<AnimalUserAdopt> updateAnimalByAnimalId(User user, AnimalUserAdopt modifiedAnimalUserAdopt,
                                                              int animalAdoptId) {

        AnimalUserAdopt animalUserAdopt = animalUserAdoptRepository.findById(animalAdoptId);
        boolean auth = animalUserAdopt.getAuth(user.getId());

        if (auth) {
            animalUserAdopt.setName(modifiedAnimalUserAdopt.getName());
            animalUserAdopt.setGender(modifiedAnimalUserAdopt.getGender());
            animalUserAdopt.setKind(modifiedAnimalUserAdopt.getKind());
            animalUserAdopt.setBirth(modifiedAnimalUserAdopt.getBirth());
            animalUserAdopt.setWeight(modifiedAnimalUserAdopt.getWeight());
            animalUserAdopt.setNeuterYn(modifiedAnimalUserAdopt.isNeuterYn());

            animalUserAdoptRepository.save(animalUserAdopt);

            List<AnimalCheckup> animalCheckupList = animalCheckupRepository.findByAnimalUserAdoptId(animalAdoptId);

            String modifiedInoculationArray[] = modifiedAnimalUserAdopt.getInoculationArray();

            //현재 등록되있는 예방접종 리스트 전부 지우고
            animalCheckupRepository.deleteByAnimalUserAdoptId(animalAdoptId);

            //받은값 토대로 다시 등록
            for(int i=0 ; i<modifiedInoculationArray.length ; i++){
                animalCheckupRepository.save(AnimalCheckup.builder()
                                            .inoculation(modifiedInoculationArray[i])
                                            .animalUserAdopt(animalUserAdopt)
                                            .build());
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER_ANIMAL, animalUserAdoptRepository.save(animalUserAdopt));
        } else {
            return DefaultRes.UNAUTHORIZATION;
        }
    }


    //사용자 좋아요 리스트 조회
    public DefaultRes<List<ListformDto>> readMyLikeList(User user) {
        List<UserAnimalLike> userAnimalLikeList = user.getUserAnimalLikeList();

        List<ListformDto> animalLikedDtoList = new ArrayList<>();

        for(UserAnimalLike userAnimalLike : userAnimalLikeList) {
            ListformDto animalListformDto = userAnimalLike.getListformDto();
            animalListformDto.setThumbnailImg(new StringBuilder(s3Endpoint).append(userAnimalLike.getAnimal().getThumbnailImg()).toString());

            //좋아요 한 녀석들만 가져오므로
            animalListformDto.setLiked(true);
            animalListformDto.setRemainDateState(animalService.getDdayState(userAnimalLike.getAnimal().getNoticeEddt()));;


            animalLikedDtoList.add(animalListformDto);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_LIKE, animalLikedDtoList);
    }

    //사용자 스크랩 리스트 조회
    public DefaultRes<List<UserCardnewsScrap>> readMyClipsList(User user) {
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP, user.getUserCardnewsScrapList());
    }

    //사용자 작성한 글 리스트 조회
    public DefaultRes<List<CommunityDto>> readMyCommunityList(User user) {

        List<Community> communityList = user.getCommunityList();
        List<CommunityDto> communityDtoList = new ArrayList<>();

        for(Community community : communityList) {
            CommunityDto communityDto = community.getCommunityDto();
            communityDto = communityService.setCommunityDtoAuthAndProfileImgWithUser(user, community, communityDto);
            communityDtoList.add(communityDto);
        }


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_COMMUNITY, communityDtoList);
    }

    //우체통 리스트 조회
    public DefaultRes<List<Mailbox>> readMailboxes(User user) {
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAILBOX, user.getMailboxList());
    }


}






