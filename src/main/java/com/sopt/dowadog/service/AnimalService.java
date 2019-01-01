package com.sopt.dowadog.service;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import com.amazonaws.util.DateUtils;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.AnimalDetailDto;
import com.sopt.dowadog.model.dto.FilterDto;
import com.sopt.dowadog.repository.*;
import com.sopt.dowadog.specification.AnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.spel.ast.OpInc;
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



@Value("${cloud.aws.endpointUrl}")
private String defaultUrl;


    public AnimalService(final AnimalRepository animalRepository,
                         final HashtagAnimalRepository hashtagAnimalRepository,
                         final UserAnimalLikeRepository userAnimalLikeRepository,
                         final UserRepository userRepository,
                         final AnimalStoryRepository animalStoryRepository){
        this.animalRepository = animalRepository;
        this.hashtagAnimalRepository = hashtagAnimalRepository;
        this.userAnimalLikeRepository = userAnimalLikeRepository;
        this.userRepository = userRepository;
        this.animalStoryRepository = animalStoryRepository;
    }

    //좋아요 여부 구현 메소드
    private boolean getUserLikeState(final String userId, final int animalId){

        List<UserAnimalLike> countingList = userAnimalLikeRepository.findAllByUser_IdAndAnimal_Id(userId,animalId);

        if(countingList.size()==0){// 좋아요 안 됨
            return false;
        }else{// 좋아요 돼있음

            return true;
        }

    }

    //썸네일 파일패스 s3 디폴트유알엘 붙인 거로 구현한 메소드
    private String getThumnailImg(final String thumbnailImgPath ){

        if(thumbnailImgPath!=null){

            String fileUrl = new StringBuilder(defaultUrl).append("/animal/").append(thumbnailImgPath).toString();
            return  fileUrl;
        }else{

            return null;
        }

    }


    //디데이가 15일 이내인지 아닌지에 대한 여부 메소드 구현
    private boolean getDdayState(final LocalDate noticeEddt){
        Period period = Period.between(LocalDate.now(),noticeEddt);

        if(period.getYears()==0 && period.getMonths() ==0 && period.getDays()>=1 && period.getDays()<=15){

            return true;
        }else{

            return false;
        }
    }





//유기동물 상세보기
    //todo 좋아요 유저 인덱스 하드로 박은 거 말고 구현
    public DefaultRes<AnimalDetailDto> readAnimal(final int animalId){


        Animal animal = animalRepository.findById(animalId).get();



        //스토리 파일 엔드포인트 붙힌 거로 배열 작업
        List<AnimalStory> animalStories = animalStoryRepository.findAllByAnimal_Id(animalId);
        List<String> totalStoryList = new ArrayList<>();

        for(AnimalStory a : animalStories){

            String temp = new StringBuilder(defaultUrl).append("/animal/").append(a.getFilePath()).toString();
            totalStoryList.add(temp);

        }



        AnimalDetailDto animalDetailDto = animal.getAnimalDetailDto();


        animalDetailDto.setAnimalStoryList(totalStoryList);
        animalDetailDto.setThumbnailImg(getThumnailImg(animal.getThumbnailImg()));
        animalDetailDto.setLiked(getUserLikeState("1",animalId));
        animalDetailDto.setRemainDateState(getDdayState(animal.getNoticeEddt()));















        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalDetailDto);

    }

//긴급한 순으로 동물 리스트보기
    //todo 좋아요 구현
    public DefaultRes<Page<Animal>> readEmergencyAnimal(int page, int limit){
        Pageable pageable = PageRequest.of(page,limit);


        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAllByOrderByNoticeEddtAsc(pageable));
    }


//필터 리스트 동물 보기
    public DefaultRes<Page<Animal>> readAnimal(final FilterDto filterDto, final int page, final int limit){
        Map<String, Object> filter = new HashMap<>();
        Pageable pageable = PageRequest.of(page, limit);

        //todo 최신순 정렬
        if (filterDto.getType() != null) filter.put("type", filterDto.getType());
        if (filterDto.getRegion() != null) filter.put("region", filterDto.getRegion());
        if (filterDto.getRemainNoticeDate() != null) filter.put("remainNoticeDate", filterDto.getRemainNoticeDate());
        if (filterDto.getSearchWord() != null) filter.put("searchWord", filterDto.getSearchWord());
        if (filterDto.isStory()) filter.put("story", filterDto.isStory());

        //지역, 보호소, 품종

        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAll(AnimalSpecification.searchAnimal(filter),pageable));



    }

// 해시 태그 리스트 보기
    public DefaultRes<Page<HashtagAnimal>> readHashtagAnimalList(final String tag, final int page, final int limit){


        Pageable pageable = PageRequest.of(page, limit,Sort.by(Sort.Direction.DESC,"animal.createdAt"));

        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL, hashtagAnimalRepository.findAllByHashtag_Keyword(tag,pageable));



    }


    // 좋아요 취소, 생성 구현
    @Transactional
    public DefaultRes<UserAnimalLike> createUserAnimalLike(final int animalIdx){

        //유저 인덱스 바꿔줘야 함(지금은 디폴트로 넣어줌)

        List<UserAnimalLike> countingList = userAnimalLikeRepository.findAllByUser_IdAndAnimal_Id("1",animalIdx);
        if(countingList.size()==0){// 좋아요 추가 기능 구현

            Optional<User> user = userRepository.findById("1");// 유저가 없다면..? 처리
            Optional<Animal> animal = animalRepository.findById(animalIdx);//에니멀이 없다면??



            //동물이 없을 때
            if(!animal.isPresent()){

                return DefaultRes.res(StatusCode.NO_CONTENT,ResponseMessage.NOT_FOUND_ANIMAL);
            }


            UserAnimalLike userAnimalLike = new UserAnimalLike();
            userAnimalLike.setAnimal(animal.get());
            userAnimalLike.setUser(user.get());

            userAnimalLikeRepository.save(userAnimalLike);

        }else{// 좋아요 취소 구현

            userAnimalLikeRepository.deleteByUser_idAndAnimal_Id("1",animalIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETED_LIKE);



        }

        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_LIKE);

    }







}
