package com.sopt.dowadog.api;


import com.sopt.dowadog.annotation.Auth;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/animals")
@Controller
public class AnimalController {

    @Autowired
    AnimalService animalService;


    //유기동물 상세 조회
    @GetMapping("/{animalId}")
    public ResponseEntity readAnimal(@PathVariable("animalId") final int animalId){
        return new ResponseEntity(animalService.readAnimal(animalId), HttpStatus.OK);

    }

    //@Auth
    //@PostMapping("/{animalId}/likes")
    //public ResponseEntity createAnimalLike(@)

    @GetMapping("/emergency")
    public ResponseEntity readEmergencyAnimal(@RequestParam(name="page", defaultValue="0",required=false)int page,
                                              @RequestParam(name="limit", defaultValue="10", required=false)int limit)
    {
      return new ResponseEntity(animalService.readEmergencyAnimal(page,limit),HttpStatus.OK);
    }

    /*@GetMapping()
    public ResponseEntity readLatestAnimal(,@RequestParam(name="page", defaultValue="0",required=false)int page,
                                           @RequestParam(name="limit", defaultValue="10", required=false)int limit ){

        return new ResponseEntity(animalService.readAnimal())
    }*/

}
