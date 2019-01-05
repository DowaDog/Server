package com.sopt.dowadog.controller.openapi;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.PublicAnimal;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.PublicAnimalListDto;
import com.sopt.dowadog.model.dto.PublicAnimalSearchDto;
import com.sopt.dowadog.service.PublicAnimalService;
import com.sopt.dowadog.service.UserService;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Cacheable("snapshotData")
@Controller
@RequestMapping("openapi/animals")
public class PublicAnimalController {






    @Autowired
    private UserService userService;

    @Autowired
    private PublicAnimalService publicAnimalService;


    //긴급 동물 리스트
    @GetMapping("/emergency")
    public ResponseEntity readEmergencyPublicAnimalList(@RequestHeader(name = "Authorization", required = false) final String jwtToken,
                                                            @RequestParam(name="page", defaultValue="0",required=false)final int page,
                                                            @RequestParam(name="limit", defaultValue="10", required=false)final int limit
                                                            ){
        try{

            User user = null;

            if(jwtToken!=null){

                user = userService.getUserByJwtToken(jwtToken);

            }
            return new ResponseEntity(publicAnimalService.readEmergencyAnimalList(user,page,limit),HttpStatus.OK);


        }catch(Exception e){
            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }









    @GetMapping
    public ResponseEntity readPublicAnimalList(@RequestHeader(name = "Authorization",required = false) final String jwtToken,
                                               @ModelAttribute PublicAnimalSearchDto search,
                                               @RequestParam(name="page", defaultValue="0",required=false)final int page,
                                               @RequestParam(name="limit", defaultValue="10", required=false)final int limit
                                              ) {

        try{
            User user = null;
            System.out.println("들어왔니"+search.getType());
            System.out.println("들어왔니"+search.getRegion());
            System.out.println("들어왔니"+search.getRemainNoticeDate());

            if(jwtToken!=null){
                System.out.print(1);
                user = userService.getUserByJwtToken(jwtToken);
            }
            //if(Optional.ofNullable(publicAnimalService.readPublicAnimalList(search, page, limit,user)).isPresent())
            return new ResponseEntity(publicAnimalService.readPublicAnimalList(search, page, limit,user), HttpStatus.OK);

        }catch(Exception e){
            e.printStackTrace();
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
