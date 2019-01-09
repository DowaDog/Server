package com.sopt.dowadog.service.admin;


import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Hashtag;
import com.sopt.dowadog.model.domain.HashtagAnimal;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.HashtagAnimalRepository;
import com.sopt.dowadog.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminHashtagService {

    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    HashtagAnimalRepository hashtagAnimalRepository;


    // 해시태그 키워드 출력기능
    public List<Hashtag> readHashtagList() {

        return hashtagRepository.findAll();

    }

    public void createHashtag(int animalId, int hashtagId){

        Animal animal = animalRepository.findById(animalId).get();
        Hashtag hashtag = hashtagRepository.findById(hashtagId).get();

        HashtagAnimal hashtagAnimal = new HashtagAnimal();
        hashtagAnimal.setAnimal(animal);
        hashtagAnimal.setHashtag(hashtag);

        hashtagAnimalRepository.save(hashtagAnimal);
    }
}
