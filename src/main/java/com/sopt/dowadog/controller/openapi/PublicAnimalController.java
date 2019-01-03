package com.sopt.dowadog.controller.openapi;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.PublicAnimalSearchDto;
import com.sopt.dowadog.scheduler.PublicAnimalScheduler;
import com.sopt.dowadog.service.PublicAnimalService;
import com.sopt.dowadog.service.UserService;
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
    private UserService userService;

    @Autowired
    private PublicAnimalService publicAnimalService;



    //긴급 동물 리스트
    //@GetMapping()









    @GetMapping
    public ResponseEntity readPublicAnimalList(@RequestHeader(name = "Authorization",required = false) final String jwtToken,
                                               @ModelAttribute PublicAnimalSearchDto search,
                                               @RequestParam(name="page", defaultValue="0",required=false)final int page,
                                               @RequestParam(name="limit", defaultValue="10", required=false)final int limit
                                              ) {

        try{
            User user = null;
            if(jwtToken!=null){
                user = userService.getUserByJwtToken(jwtToken);
            }
            return new ResponseEntity(publicAnimalService.readPublicAnimalList(search, page, limit,user), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{animalId}")
    public ResponseEntity readPublicAnimalDetail(@RequestHeader(name = "Authorization",required = false)final String jwtToken,
                                                 @PathVariable(name = "animalId") final int animalId){


        try{
            User user = null;
            if(jwtToken!=null){
                user = userService.getUserByJwtToken(jwtToken);
            }

            return new ResponseEntity(publicAnimalService.readPublicAnimal(user ,animalId),HttpStatus.OK);


        }catch(Exception e){
            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

}
