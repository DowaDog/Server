package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.annotation.Verify;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.service.CardnewsContentsService;
import com.sopt.dowadog.service.CardnewsService;
import com.sopt.dowadog.service.UserService;
import com.sopt.dowadog.service.admin.AdminCardnewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/cardnews")
@Controller
@Verify
public class AdminCardnewsController {

    @Autowired
    UserService userService;
    @Autowired
    AdminCardnewsService adminCardnewsService;
    @Autowired
    CardnewsContentsService cardnewsContentsService;

    //카드뉴스 대분류 작성
    @PostMapping
    public ResponseEntity createCardnews(Cardnews cardnews){
        return new ResponseEntity(adminCardnewsService.createCardnewsService(cardnews), HttpStatus.CREATED);
    }

    //카드뉴스 대분류 수정
    @PostMapping("{cardnewsId}")
    public ResponseEntity updateCardnews(Cardnews cardnews, @PathVariable("cardnewsId")int cardnewsId) {
        return new ResponseEntity(adminCardnewsService.updateCardnewsById(cardnews, cardnewsId), HttpStatus.OK);
    }

    //카드뉴스 대분류 삭제
    @DeleteMapping("{cardnewsId}")
    public ResponseEntity deleteCardnews(@PathVariable("cardnewsId")int cardnewsId){
        return new ResponseEntity((adminCardnewsService.deleteCardnewsById(cardnewsId)),HttpStatus.OK);
    }

    //카드뉴스 컨텐츠 작성
    @PostMapping("{cardnewsId}/contents")
    public ResponseEntity createCardnewsContennts(CardnewsContents cardnewsContents, @PathVariable("cardnewsId")int cardnewsId){
        return new ResponseEntity(adminCardnewsService.createCardnewsContentsService(cardnewsContents,cardnewsId), HttpStatus.CREATED);
    }

    //카드뉴스 컨텐츠 수정
    @PostMapping("contents/{contentsId}")
    public ResponseEntity updateCardnewsContents(CardnewsContents cardnewsContents, @PathVariable("contentsId")int contentsId){
        return new ResponseEntity(adminCardnewsService.updateCardnewsContentsById(cardnewsContents, contentsId), HttpStatus.OK);
    }

    @DeleteMapping("contents/{contentsId}")
    public ResponseEntity deleteCardnewsContents(@PathVariable("contentsId")int contentsId){
        return new ResponseEntity(adminCardnewsService.deleteCardnewsContentsById(contentsId), HttpStatus.OK);
    }

}
