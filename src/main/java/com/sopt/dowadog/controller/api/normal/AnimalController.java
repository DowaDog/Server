package com.sopt.dowadog.controller.api.normal;


import com.sopt.dowadog.model.dto.FilterDto;
import com.sopt.dowadog.service.AnimalService;
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


    //유기동물 상세 조회
    @GetMapping("{animalId}")
    public ResponseEntity readAnimal(@PathVariable("animalId") final int animalId){

        return new ResponseEntity(animalService.readAnimal(animalId,"1"), HttpStatus.OK);

    }

    //@Auth
    //@PostMapping("/{animalId}/likes")
    //public ResponseEntity createAnimalLike(@)

    @GetMapping("emergency")
    public ResponseEntity readEmergencyAnimal(@RequestParam(name="page", defaultValue="0",required=false)int page,
                                              @RequestParam(name="limit", defaultValue="10", required=false)int limit)
    {
      return new ResponseEntity(animalService.readEmergencyAnimal(page,limit,"1"),HttpStatus.OK);
    }



    //유기동물 필터 넣기
    @GetMapping()
    public ResponseEntity readLatestAnimal(@ModelAttribute FilterDto filterDto, @RequestParam(name="page", defaultValue="0",required=false)int page,
                                           @RequestParam(name="limit", defaultValue="10", required=false)int limit ){


        return new ResponseEntity(animalService.readAnimal(filterDto,page,limit,"1"),HttpStatus.OK);
    }

    //해쉬태그에 대한 검색

    @GetMapping("/hashtags")
    public ResponseEntity readHashtagAnimal(@RequestParam(name="tag", required=false) String tag, @RequestParam(name="page", defaultValue="0",required=false)int page,
                                            @RequestParam(name="limit", defaultValue="10", required=false)int limit ){


        return new ResponseEntity(animalService.readHashtagAnimalList(tag,page,limit,"1"),HttpStatus.OK);
    }

    // 좋아요 기능
    //todo 토큰이 들어가면, 유저인덱스 넣는 부분
    @PostMapping("{animalId}/likes")
    public ResponseEntity createUserLike(@PathVariable("animalId") final int animalId){
        return new ResponseEntity(animalService.createUserAnimalLike("1",animalId),HttpStatus.OK);
    }
}