package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    AnimalRepository animalRepository;

    public DefaultRes<Animal> readAnimal(final int animalId){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, animalRepository.findById(animalId).get());

    }
    //todo 페이저블 처리
    public DefaultRes<List<Animal>> readEmergencyAnimal(){
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_ANIMAL,animalRepository.findAllByOrderByNoticeEddtDesc());
    }



}
