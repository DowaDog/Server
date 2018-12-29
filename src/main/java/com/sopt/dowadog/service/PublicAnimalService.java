package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.dto.PublicAnimalSearchDto;
import com.sopt.dowadog.repository.PublicAnimalRepository;
import com.sopt.dowadog.specification.PublicAnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PublicAnimalService {

    @Autowired
    PublicAnimalRepository publicAnimalRepository;


    //todo 캐시적용 해야함 ( 스케줄링타이밍동안 문제 없도록 신경써야함 )
    public DefaultRes readPublicAnimalList(PublicAnimalSearchDto search) {
        Map<String, Object> filter = new HashMap<>();


        if (search.getType() != null) filter.put("type", search.getType());
        if (search.getRegion() != null) filter.put("region", search.getRegion());
        if (search.getRemainNoticeDate() != null) filter.put("remainNoticeDate", search.getRemainNoticeDate());


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, publicAnimalRepository.findAll(PublicAnimalSpecification.searchPublicAnimal(filter)));
    }


}
