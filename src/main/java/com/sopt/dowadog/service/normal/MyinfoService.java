package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.*;
import com.sopt.dowadog.repository.AnimalCheckupRepository;
import com.sopt.dowadog.repository.AnimalUserAdoptRepository;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.common.CodeService;
import com.sopt.dowadog.service.common.FileService;
import com.sopt.dowadog.repository.*;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

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
    @Autowired
    UserCardnewsScrapRepository userCardnewsScrapRepository;
    @Autowired
    UserAnimalLikeRepository userAnimalLikeRepository;
    @Autowired
    CardnewsService cardnewsService;


    @Value("${uploadpath.myinfo}")
    private String baseDir;
    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    //todo 우체통 API작성 controller 작성하기 테이블도 구성되야함



    //UserID로 정보가져오기
    public DefaultRes<MyinfoDto> readMypage(User user) {

        System.out.println("readMyPage COME!");

        MyinfoDto myinfoDto = user.getMyinfoDto();
        myinfoDto.setProfileImg(S3Util.getImgPath(s3Endpoint, user.getProfileImg()));

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MYINFO, myinfoDto);
    }

    //사용자 정보 수정
    public DefaultRes updateUserInfo(User user, MyinfoChangeDto myinfoChangeDto, MultipartFile profileImgFile) {



        if (profileImgFile != null) {
            String filePath = S3Util.getFilePath(baseDir, profileImgFile);

            fileService.fileUpload(profileImgFile, filePath);
            user.setProfileImg(filePath);
        } else{
           String temp = user.getProfileImg();

           user.setProfileImg(temp);
        }

        user.setName(myinfoChangeDto.getName());
        user.setPhone(myinfoChangeDto.getPhone());
        user.setBirth(myinfoChangeDto.getBirth());

        userRepository.save(user);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_MYINFO);
    }

    //입양한 동물 리스트 //todo 리스폰스메세지 변경
    public DefaultRes<List<AnimalUserAdopt>> readAnimalUserAdoptList(User user) {
        List<AnimalUserAdopt> animalUserAdoptList = user.getAnimalUserAdoptList();

        for(AnimalUserAdopt animalUserAdopt : animalUserAdoptList) {
            animalUserAdopt.setProfileImg(S3Util.getImgPath(s3Endpoint, animalUserAdopt.getProfileImg()));
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalUserAdoptList);
    }

    //입양한 동물 정보 조회
    public DefaultRes<AdoptAnimalDto> readAnimalUserAdoptById(User user, int adoptAnimalId) {

        AnimalUserAdopt animalUserAdopt = animalUserAdoptRepository.findById(adoptAnimalId);
        animalUserAdopt.setProfileImg(new StringBuilder(s3Endpoint).append(animalUserAdopt.getProfileImg()).toString());
        //adopAnimalId를 가진 동물 조회
        AdoptAnimalDto animalDto = AdoptAnimalDto.builder()
                        .animalUserAdopt(animalUserAdopt).build();

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
        System.out.println(user.getId());
        System.out.println("######## Auth");
        System.out.println(auth);
        System.out.println(modifiedAnimalUserAdopt.getInoculationArray()[0]);

        if (auth) {
            animalUserAdopt.setName(modifiedAnimalUserAdopt.getName());
            animalUserAdopt.setGender(modifiedAnimalUserAdopt.getGender());
            animalUserAdopt.setKind(modifiedAnimalUserAdopt.getKind());
            animalUserAdopt.setAge(modifiedAnimalUserAdopt.getAge());
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
            System.out.println("예방접종 리스트 업데이트");

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER_ANIMAL, animalUserAdoptRepository.save(animalUserAdopt));
        } else {
            return DefaultRes.UNAUTHORIZATION;
        }
    }


    //사용자 좋아요 리스트 조회

    public DefaultRes<List<ListformDto>> readMyLikeList(User user){

        try{
            List<ListformDto> animalLikeDtoList = new ArrayList<>();

            for(UserAnimalLike temp : userAnimalLikeRepository.findAllByUser_IdOrderByCreatedAtDesc(user.getId())){
                ListformDto listformDto = ListformDto.builder()
                        .noticeEddt(temp.getAnimal().getNoticeEddt())
                        .thumbnailImg(S3Util.getImgPath(s3Endpoint,temp.getAnimal().getThumbnailImg()))
                        .liked(animalService.getLikedForGuest(user,temp.getAnimal().getId()))//함수사용
                        .remainDateState(animalService.getDdayState(temp.getAnimal().getNoticeEddt()))//함수사용
                        .id(temp.getAnimal().getId())
                        .kindCd(temp.getAnimal().getKindCd())
                        .processState(temp.getAnimal().getProcessState())
                        .education(cardnewsService.getAllEducatedDtoComplete(user).isAllComplete())// 카드뉴스에 있는 거 가져오기
                        .type(temp.getAnimal().getType())
                        .sexCd(temp.getAnimal().getSexCd())
                        .region(temp.getAnimal().getCare().getRegion())
                        .build();
                animalLikeDtoList.add(listformDto);
            }
            return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_LIKE,animalLikeDtoList);


        }catch (Exception e){
            e.printStackTrace();
            return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
        }
        }

    //사용자 스크랩 리스트 조회
    public DefaultRes<List<MyinfoScrapListDto>> readMyClipsList(User user) {
        try{
            List<MyinfoScrapListDto> tempList = new ArrayList<>();

            for(UserCardnewsScrap temp : userCardnewsScrapRepository.findAllBy(user.getId())){

                MyinfoScrapListDto myinfoScrapListDto = MyinfoScrapListDto
                        .builder()
                        .id(temp.getCardnews().getId())
                        .title(temp.getCardnews().getTitle())
                        .createAt(temp.getCreatedAt())
                        .build();
                tempList.add(myinfoScrapListDto);

            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP,tempList);

        }catch (Exception e){
            e.printStackTrace();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP);


        }

//        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP, userCardnewsScrapRepository.findAllBy(user.getId()));
    }

    //사용자 작성한 글 리스트 조회
    public DefaultRes<List<MyinfoCommunityDto>> readMyCommunityList(User user) {




        Optional<List<Community>> communityList = communityService.readMyCommunity(user);
        List<MyinfoCommunityDto> communityDtoList = new ArrayList<>();


            for(Community community : communityList.get()) {
                MyinfoCommunityDto communityDto = MyinfoCommunityDto.builder()
                        .id(community.getId())
                        .title(community.getTitle())
                        .createAt(community.getCreatedAt())
                        .updateAt(community.getUpdatedAt())
                        .build();

                communityDtoList.add(communityDto);
            }


            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_COMMUNITY, communityDtoList);


    }

    //우체통 리스트 조회
    public DefaultRes<List<MailboxDto>> readMailboxes(User user) {

        List<MailboxDto> mailboxDtoList = new ArrayList<>();

        System.out.println("mailbox list select come");
        for(Mailbox mailbox : user.getMailboxList()) {
            mailboxDtoList.add(mailbox.getMailboxDto());
        }
        System.out.println("mailboxDTO setted");

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAILBOX, mailboxDtoList);
    }

    public DefaultRes<MyinfoChangeDto> readMyinfo(final User user){
        try {
            User temp = userRepository.findById(user.getId()).get();

            MyinfoChangeDto myinfoChangeDto = MyinfoChangeDto.builder()
                    .thumbnailImg(S3Util.getImgPath(s3Endpoint,temp.getProfileImg()))
                    .birth(temp.getBirth())
                    .name(temp.getName())
                    .phone(temp.getPhone())
                    .build();

            return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_USER,myinfoChangeDto);


        }catch (Exception e){
            e.printStackTrace();
            return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
        }
    }


}






