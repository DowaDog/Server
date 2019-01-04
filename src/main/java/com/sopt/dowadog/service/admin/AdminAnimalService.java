package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.service.FileService;
import com.sopt.dowadog.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@Service
public class AdminAnimalService {

    @Autowired
    FileService fileService;
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    CareRepository careRepository;

    @Value("${uploadpath.animal}")
    private String baseDir;


//
    public void createAnimal(Animal animal, int careId){

        if(animal.getThumbnailImgFile() != null){


            String filePath = S3Util.getFilePath(baseDir, animal.getThumbnailImgFile());

            System.out.println("####");
            System.out.println(filePath);

            fileService.fileUpload(animal.getThumbnailImgFile(), filePath);

            animal.setThumbnailImg(filePath);
        }

        Care care = careRepository.findById(careId).get();
        animal.setCare(care);

        animalRepository.save(animal);

    }
}
