package com.sopt.dowadog.controller.openapi;

import com.sopt.dowadog.model.dto.PublicAnimalSearchDto;
import com.sopt.dowadog.scheduler.PublicAnimalScheduler;
import com.sopt.dowadog.service.PublicAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Cacheable("snapshotData")
@Controller
@RequestMapping("openapi/animals")
public class PublicAnimalController {

    @Autowired
    private PublicAnimalService publicAnimalService;





    @GetMapping
    public ResponseEntity readPublicAnimalList(@ModelAttribute PublicAnimalSearchDto search,
                                               @RequestParam(name="page", defaultValue="0",required=false)int page,
                                               @RequestParam(name="limit", defaultValue="10", required=false)int limit
                                              ) {



        return new ResponseEntity(publicAnimalService.readPublicAnimalList(search, page, limit,"1"), HttpStatus.OK);


    }

    @GetMapping("/{animalId}")
    public ResponseEntity readPublicAnimalDetail(@PathVariable(name = "animalId") final int animalId){
        return new ResponseEntity(publicAnimalService.readPublicAnimal("1",animalId),HttpStatus.OK);
    }

}
