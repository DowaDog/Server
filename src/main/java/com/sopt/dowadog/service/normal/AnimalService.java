package com.sopt.dowadog.service.normal;

import com.amazonaws.services.s3.internal.S3AbortableInputStream;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.*;
import com.sopt.dowadog.repository.*;
import com.sopt.dowadog.specification.AnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class AnimalService {



    private final AnimalRepository animalRepository;
    private final HashtagAnimalRepository hashtagAnimalRepository;
    private final UserAnimalLikeRepository userAnimalLikeRepository;
    private final UserRepository userRepository;
    private final AnimalStoryRepository animalStoryRepository;
    private final CareRepository careRepository;
    private final CardnewsService cardnewsService;



@Value("${cloud.aws.endpoint}")
private String defaultUrl;


    public AnimalService(final AnimalRepository animalRepository,
                         final HashtagAnimalRepository hashtagAnimalRepository,
                         final UserAnimalLikeRepository userAnimalLikeRepository,
                         final UserRepository userRepository,
                         final AnimalStoryRepository animalStoryRepository,
                         final CareRepository careRepository,
                         final CardnewsService cardnewsService){
        this.animalRepository = animalRepository;
        this.hashtagAnimalRepository = hashtagAnimalRepository;
        this.userAnimalLikeRepository = userAnimalLikeRepository;
        this.userRepository = userRepository;
        this.animalStoryRepository = animalStoryRepository;
        this.careRepository = careRepository;
        this.cardnewsService = cardnewsService;
    }

    //좋아요 여부 구현 메소드
    public boolean getUserLikeState(final String userId, final int animalId){


        List<UserAnimalLike> countingList = userAnimalLikeRepository.findAllByUser_IdAndAnimal_Id(userId,animalId);

        if(countingList.size()==0){// 좋아요 안 됨
            return false;
        }else{// 좋아요 돼있음

            return true;
        }

    }

    //썸네일 파일패스 s3 디폴트유알엘 붙인 거로 구현한 메소드
    public String getThumnailImg(final String thumbnailImgPath ){

        if(thumbnailImgPath!=null){

            String fileUrl = S3Util.getImgPath(defaultUrl,thumbnailImgPath);
            return  fileUrl;
        }else{

            return null;
        }

    }


    //디데이가 15일 이내인지 아닌지에 대한 여부 메소드 구현
    public boolean getDdayState(final LocalDate noticeEddt){
        Period period = Period.between(LocalDate.now(),noticeEddt);

        if(period.getYears()==0 && period.getMonths() ==0 && period.getDays()>=1 && period.getDays()<=15){

            return true;
        }else{

            return false;
        }
    }

    //리스트 포멧으로 바꾸는 메소드
    private AnimalListDto getAnimalListDto(final List<Animal> animalList ,final Pageable pageable,final User user  ){
        List<ListformDto> listform = new ArrayList<>();


        AllEducatedDto allEducatedDto = cardnewsService.getAllEducatedDtoComplete(user);

  //      if(cardnewsService.getAllEducatedDtoComplete(user))

        for(Animal temp : animalList){

            ListformDto listformDto = temp.getListAnimalDto();
            listformDto.setRemainDateState(getDdayState(temp.getNoticeEddt()));
            listformDto.setThumbnailImg(getThumnailImg(temp.getThumbnailImg()));
            listformDto.setLiked(getLikedForGuest(user,temp.getId()));
            listformDto.setEducation(allEducatedDto.isAllComplete());
            listform.add(listformDto);
        }



        AnimalListDto animalListDto = AnimalListDto.builder()
                .pageable(pageable)
                .content(listform)
                .build();

        return animalListDto;

    }

    // 토큰 null 일 때 좋아요(게스트) 처리
    public boolean getLikedForGuest (final User user,final int animalId){
        boolean stateLike;

        if(user==null){

            stateLike = false;


        }else{
            stateLike = getUserLikeState(user.getId(),animalId);

        }

        return stateLike;

    }





//유기동물 상세보기

    public DefaultRes<AnimalDetailDto> readAnimal(final int animalId, final User user){
        AllEducatedDto allEducatedDto = cardnewsService.getAllEducatedDtoComplete(user);



        Optional<Animal> animalTemp = animalRepository.findById(animalId);
        if(!animalTemp.isPresent()){
            return DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NOT_FOUND_ANIMAL);

        }

        Animal animal = animalTemp.get();

        //스토리 파일 엔드포인트 붙힌 거로 배열 작업
        List<AnimalStory> animalStories = animalStoryRepository.findAllByAnimal_Id(animalId);
        List<String> totalStoryList = new ArrayList<>();
        List<String> totalStoryListAos = new ArrayList<>();

        for(AnimalStory a : animalStories){

            if(a.getFilePath()!=null){
                String temp = S3Util.getImgPath(defaultUrl,a.getFilePath());
                totalStoryList.add(temp);
            }
            if(a.getFilePathAos()!= null){
                String tempAos = S3Util.getImgPath(defaultUrl,a.getFilePathAos());
                totalStoryListAos.add(tempAos);

            }

        }



        AnimalDetailDto animalDetailDto = animal.getAnimalDetailDto();


        animalDetailDto.setAnimalStoryList(totalStoryList);
        animalDetailDto.setThumbnailImg(getThumnailImg(animal.getThumbnailImg()));
        animalDetailDto.setLiked(getLikedForGuest(user,animalId));
        animalDetailDto.setRemainDateState(getDdayState(animal.getNoticeEddt()));
        animalDetailDto.setEducationState(allEducatedDto.isAllComplete());
        animalDetailDto.setAnimalStoryListAos(totalStoryListAos);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalDetailDto);

    }

//긴급한 순으로 동물 리스트보기
    public DefaultRes<AnimalListDto> readEmergencyAnimal(final int page, final int limit, final User user){

        Pageable pageable = PageRequest.of(page,limit);
        Page<Animal> animals = animalRepository.findAllBy(LocalDate.now(),"notice",pageable);


        List<Animal> animalList = animals.getContent();




        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,getAnimalListDto(animalList,pageable,user));
    }


//필터 리스트 동물 보기
    public DefaultRes<AnimalListDto> readAnimal(final FilterDto filterDto, final int page, final int limit, final User user){
        Map<String, Object> filter = new HashMap<>();
        Pageable pageable = PageRequest.of(page, limit,Sort.by(Sort.Direction.DESC,"createdAt"));


        System.out.println("#### 필터 리스트 동물보기 ####");
        System.out.println(filterDto.toString());


        //todo 최신순 정렬
        if (filterDto.getType() != null) filter.put("type", filterDto.getType());
        if (filterDto.getRegion() != null) filter.put("region", filterDto.getRegion());
        if (filterDto.getRemainNoticeDate() != null) filter.put("remainNoticeDate", filterDto.getRemainNoticeDate());
        if (filterDto.getSearchWord() != null) filter.put("searchWord", filterDto.getSearchWord());
        if (filterDto.isStory()) filter.put("story", filterDto.isStory());

        //지역, 보호소, 품종

        Page<Animal> animals = animalRepository.findAll(AnimalSpecification.searchAnimal(filter),pageable);
        List<Animal> animalList = animals.getContent();


        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,getAnimalListDto(animalList,pageable,user));



    }

// 해시 태그 리스트 보기
    public DefaultRes<AnimalListDto> readHashtagAnimalList(final String tag, final int page, final int limit, final User user){


        AllEducatedDto allEducatedDto = cardnewsService.getAllEducatedDtoComplete(user);


        List<ListformDto> listform = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, limit,Sort.by(Sort.Direction.DESC,"animal.createdAt"));

        Page<HashtagAnimal> animalContents = hashtagAnimalRepository.findAllByHashtag_Keyword(tag,LocalDate.now(),"notice",pageable);
        List<HashtagAnimal> animalList = animalContents.getContent();



        for(HashtagAnimal temp : animalList){

            ListformDto listformDto = temp.getAnimal().getListAnimalDto();
            listformDto.setRemainDateState(getDdayState(temp.getAnimal().getNoticeEddt()));
            listformDto.setThumbnailImg(getThumnailImg(temp.getAnimal().getThumbnailImg()));
            listformDto.setLiked(getLikedForGuest(user,temp.getAnimal().getId()));
            listformDto.setEducation(allEducatedDto.isAllComplete());
            listform.add(listformDto);
        }



        AnimalListDto animalListDto = AnimalListDto.builder()
                .pageable(pageable)
                .content(listform)
                .build();



        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalListDto);



    }


    // 좋아요 취소, 생성 구현
    @Transactional
    public DefaultRes<UserAnimalLike> createUserAnimalLike(final User user, final int animalIdx){

        //유저 인덱스 바꿔줘야 함(지금은 디폴트로 넣어줌)

        List<UserAnimalLike> countingList = userAnimalLikeRepository.findAllByUser_IdAndAnimal_Id(user.getId(),animalIdx);
        if(countingList.size()==0){// 좋아요 추가 기능 구현

            Optional<User> usertemp = userRepository.findById(user.getId());// 유저가 없다면..? 처리
            Optional<Animal> animal = animalRepository.findById(animalIdx);//에니멀이 없다면??



            //동물이 없을 때
            if(!animal.isPresent()){

                return DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NOT_FOUND_ANIMAL);
            }


            UserAnimalLike userAnimalLike = new UserAnimalLike();
            userAnimalLike.setAnimal(animal.get());
            userAnimalLike.setUser(usertemp.get());

            userAnimalLikeRepository.save(userAnimalLike);

        }else{// 좋아요 취소 구현

            userAnimalLikeRepository.deleteByUser_idAndAnimal_Id(user.getId(),animalIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED_LIKE);



        }

        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_LIKE);

    }


    public DefaultRes<CareDto> readCareinfoByAnimalId(int animalId){

        if(!animalRepository.findById(animalId).isPresent()) return DefaultRes.NOT_FOUND;
        Animal animal = animalRepository.findById(animalId).get();
        Care care = animal.getCare();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARE, care.getCareDto());
    }







}
