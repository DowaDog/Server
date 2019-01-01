package com.sopt.dowadog.service;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.FilterDto;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.HashtagAnimalRepository;
import com.sopt.dowadog.repository.UserAnimalLikeRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.specification.AnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnimalService {



    private final AnimalRepository animalRepository;
    private final HashtagAnimalRepository hashtagAnimalRepository;
    private final UserAnimalLikeRepository userAnimalLikeRepository;
    private final UserRepository userRepository;

/*
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    HashtagAnimalRepository hashtagAnimalRepository;
    @Autowired
    UserAnimalLikeRepository userAnimalLikeRepository;
    @Autowired
    UserRepository userRepository;
*/



    public AnimalService(final AnimalRepository animalRepository,
                         final HashtagAnimalRepository hashtagAnimalRepository,
                         final UserAnimalLikeRepository userAnimalLikeRepository,
                         final UserRepository userRepository){
        this.animalRepository = animalRepository;
        this.hashtagAnimalRepository = hashtagAnimalRepository;
        this.userAnimalLikeRepository = userAnimalLikeRepository;
        this.userRepository = userRepository;
    }




    //todo 좋아요 구현
    public DefaultRes<Animal> readAnimal(final int animalId){
        
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL,animalRepository.findById(animalId).get());

    }

    //todo 좋아요 구현
    public DefaultRes<Page<Animal>> readEmergencyAnimal(int page, int limit){
        Pageable pageable = PageRequest.of(page,limit);


        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAllByOrderByNoticeEddtAsc(pageable));
    }

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


    public DefaultRes<Page<HashtagAnimal>> readHashtagAnimalList(final String tag, final int page, final int limit){


        Pageable pageable = PageRequest.of(page, limit,Sort.by(Sort.Direction.DESC,"animal.createdAt"));

        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL, hashtagAnimalRepository.findAllByHashtag_Keyword(tag,pageable));



    }


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
