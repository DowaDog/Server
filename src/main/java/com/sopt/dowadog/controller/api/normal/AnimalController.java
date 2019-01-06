package com.sopt.dowadog.controller.api.normal;


import com.sopt.dowadog.annotation.Auth;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.FilterDto;
import com.sopt.dowadog.service.normal.AnimalService;
import com.sopt.dowadog.service.common.JwtService;
import com.sopt.dowadog.service.normal.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("api/normal/animals")
@Controller
public class AnimalController {

    @Autowired
    AnimalService animalService;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;


    //유기동물 상세 조회
    @GetMapping("{animalId}")
    public ResponseEntity readAnimal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                     @PathVariable("animalId") final int animalId) {
        try {
            User user = null;
            if (jwtToken != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }

            return new ResponseEntity(animalService.readAnimal(animalId, user), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


    @GetMapping("emergency")
    public ResponseEntity readEmergencyAnimal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                              @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {
        try {
            User user = null;
            if (jwtToken != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }

            return new ResponseEntity(animalService.readEmergencyAnimal(page, limit, user), HttpStatus.OK);


        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    //유기동물 필터 넣기
    @GetMapping()
    public ResponseEntity readLatestAnimal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                           @ModelAttribute FilterDto filterDto, @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                           @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {
        try {
            User user = null;
            if (jwtToken != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }

            return new ResponseEntity(animalService.readAnimal(filterDto, page, limit, user), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

    //해쉬태그에 대한 검색

    @GetMapping("/hashtags")
    public ResponseEntity readHashtagAnimal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                            @RequestParam(name = "tag", required = false) String tag, @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {

        try {
            User user = null;
            if (jwtToken != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }

            return new ResponseEntity(animalService.readHashtagAnimalList(tag, page, limit, user), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


    // 좋아요 기능
    @Auth
    @PostMapping("{animalId}/likes")
    public ResponseEntity createUserLike(@RequestHeader(name = "Authorization", required = false) String jwtToken,
                                         @PathVariable("animalId") final int animalId) {
        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(animalService.createUserAnimalLike(user, animalId), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("{animalId}/careinfo")
    public ResponseEntity readCareinfoByAnimalId(@PathVariable(name = "animalId") int animalId) {
        return new ResponseEntity(animalService.readCareinfoByAnimalId(animalId), HttpStatus.OK);
    }

}
