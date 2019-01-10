package com.sopt.dowadog.controller.admin;


import com.sopt.dowadog.service.admin.AdminHashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/admin/hashtag")
@Controller
public class AdminHashtagController {


    @Autowired
    AdminHashtagService adminHashtagService;

    @GetMapping
    public ResponseEntity readHashtagList(){
        return new ResponseEntity(adminHashtagService.readHashtagList(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createHashtag(@RequestParam(name = "animalId")int animalId,
                                        @RequestParam(name = "hashtagId")int hashtagId){

        adminHashtagService.createHashtag(animalId,hashtagId);
        return new ResponseEntity(HttpStatus.CREATED);

    }

}
