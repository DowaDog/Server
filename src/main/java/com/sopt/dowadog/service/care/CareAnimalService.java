package com.sopt.dowadog.service.care;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.CareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CareAnimalService {

    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    CareRepository careRepository;

    public List<Animal> readAnimalListByCare(Care care) {

        List<Animal> animalList = new ArrayList<>();

        if(animalRepository.findByCare(care).isPresent()) {
            animalList = animalRepository.findByCare(care).get();
        }
        return animalList;
    }


}
