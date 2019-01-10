package com.sopt.dowadog.controller.api.care;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.service.care.CareAnimalService;
import com.sopt.dowadog.service.care.CareRegistrationService;
import com.sopt.dowadog.service.care.CareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/care/animals")
@Controller
public class CareAnimalController {

    @Autowired
    CareService careService;
    @Autowired
    CareAnimalService careAnimalService;
    @Autowired
    CareRegistrationService careRegistrationService;


    @GetMapping
    public ResponseEntity animalList(@RequestHeader(value = "Authorization", required = false) final String jwtToken) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);
            return new ResponseEntity(careAnimalService.readAnimalListByCare(care), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("{animalId}")
    public ResponseEntity animal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                 @PathVariable(name = "animalId") int animalId) {
        try{
            return new ResponseEntity(careRegistrationService.readAnimalRegistrationByAnimalId(animalId), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);

    }
}