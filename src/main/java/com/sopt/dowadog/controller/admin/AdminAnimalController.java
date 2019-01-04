package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.service.AnimalService;
import com.sopt.dowadog.service.admin.AdminAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@RequestMapping("api/admin/animal"  )
@Controller
public class AdminAnimalController {

    @Autowired
    AdminAnimalService adminAnimalService;

    @PostMapping
    public ResponseEntity animal(Animal animal, @RequestParam("careId") int careId) {
        adminAnimalService.createAnimal(animal, careId);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
