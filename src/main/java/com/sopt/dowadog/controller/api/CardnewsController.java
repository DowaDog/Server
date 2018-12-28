package com.sopt.dowadog.controller.api;

import com.sopt.dowadog.service.CardnewsContentsService;
import com.sopt.dowadog.service.CardnewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/cardnews")
@Controller
public class CardnewsController {

    @Autowired
    CardnewsService cardnewsService;

    @Autowired
    CardnewsContentsService cardnewsContentsService;

    @GetMapping
    public ResponseEntity readCardnewsList(@RequestParam("type")String type){

        //todo 교육 이수 완료 정보 줘야함

        return new ResponseEntity(cardnewsService.readCardnewsList(type), HttpStatus.OK);
    }

    @GetMapping("/{cardnewsId}/contents")
    public ResponseEntity readAllCardnewsContentsList(@PathVariable("cardnewsId")int cardnewsId,
                                                      @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                      @RequestParam(name="limit",defaultValue = "10", required=false)int limit){

        return new ResponseEntity(cardnewsContentsService.readAllCardnewsContentsList(cardnewsId),HttpStatus.OK);
    }

//    @PostMapping("/cardnews/{cardnewsId}/complete")
//    public ResponseEntity createComplete(@)

}
