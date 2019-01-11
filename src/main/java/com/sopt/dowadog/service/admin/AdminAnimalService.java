package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.AnimalStory;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.AnimalStoryRepository;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.service.common.FileService;
import com.sopt.dowadog.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    AnimalStoryRepository animalStoryRepository;

    @Value("${uploadpath.animal}")
    private String baseDir;

    @Value("${uploadpath.animalStories}")
    private String animalStoryBaseDir;






    public void createAnimal(Animal animal, int careId){


        if(animal.getThumbnailImgFile() != null){
            String filePath = S3Util.getFilePath(baseDir, animal.getThumbnailImgFile());

            System.out.println(filePath);

            fileService.fileUpload(animal.getThumbnailImgFile(), filePath);

            animal.setThumbnailImg(filePath);
        }

        Care care = careRepository.findById(careId).get();
        animal.setCare(care);

        animalRepository.save(animal);

    }


    public List<Animal> allAnimal() {
        return animalRepository.findAll();
    }

    public void createAnimalStory(AnimalStory animalStory, int animalId){

        System.out.println("come to service");

        Animal animal = animalRepository.findById(animalId).get();

        animalStory.getAnimalStoryFilesAos();
        animalStory.getAnimalStoryFiles();


        AnimalStory tempAnimal = new AnimalStory();

        // 아이오에스 이미지 처리
        if(!animalStory.getAnimalStoryFiles().isEmpty() ) {


            String filePath = S3Util.getFilePath(animalStoryBaseDir, animalStory.getAnimalStoryFiles());

            //s3 업로드
            fileService.fileUpload(animalStory.getAnimalStoryFiles(), filePath);
            tempAnimal.setFilePath(filePath);


        }
        // 안드로이드 이미지 처리
        if(!animalStory.getAnimalStoryFilesAos().isEmpty()){

            String filePathAos = S3Util.getFilePath(animalStoryBaseDir, animalStory.getAnimalStoryFilesAos());
            fileService.fileUpload(animalStory.getAnimalStoryFilesAos(), filePathAos);
            tempAnimal.setFilePathAos(filePathAos);

        }

        tempAnimal.setAnimal(animal);
        animalStoryRepository.save(tempAnimal);

    }
}
