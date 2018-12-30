package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.dto.FilterDto;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.specification.AnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnimalService {

    @Autowired
    AnimalRepository animalRepository;

    //todo 좋아요 구현
    public DefaultRes<Animal> readAnimal(final int animalId){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalRepository.findById(animalId).get());

    }
    //todo 좋아요 구현
    public DefaultRes<Page<Animal>> readEmergencyAnimal(int page, int limit){
        Pageable pageable = PageRequest.of(page,limit);


        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAllByOrderByNoticeEddtAsc(pageable));
    }

    public DefaultRes<Page<Animal>> readAnimal(final FilterDto filterDto, final int page, final int limit){
        Map<String, Object> filter = new HashMap<>();
        Pageable pageable = PageRequest.of(page, limit);


        if (filterDto.getType() != null) filter.put("type", filterDto.getType());
        if (filterDto.getRegion() != null) filter.put("region", filterDto.getRegion());
        if (filterDto.getRemainNoticeDate() != null) filter.put("remainNoticeDate", filterDto.getRemainNoticeDate());
        if (filterDto.getSearchWord() != null) filter.put("searchWord", filterDto.getSearchWord());
        if (filterDto.isStory()) filter.put("story", filterDto.isStory());



        //지역, 보호소, 품종











        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAll(AnimalSpecification.searchAnimal(filter),pageable));



    }





}
