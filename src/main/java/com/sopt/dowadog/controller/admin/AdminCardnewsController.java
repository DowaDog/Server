
package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.service.normal.CardnewsContentsService;
import com.sopt.dowadog.service.normal.UserService;
import com.sopt.dowadog.service.admin.AdminCardnewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/admin/cardnews")
@Controller
public class AdminCardnewsController {

    @Autowired
    UserService userService;
    @Autowired
    AdminCardnewsService adminCardnewsService;
    @Autowired
    CardnewsContentsService cardnewsContentsService;

    //카드뉴스 교육 조회
    @GetMapping("education")
    public ResponseEntity readCardnewsEducationList(){

        System.out.println("#######     api/admin/cardnews/education   GET #######");

        //todo 교육 이수 완료 정보 줘야댐
        return new ResponseEntity(adminCardnewsService.readCardnewsEducationList(), HttpStatus.OK);
    }

    //카드뉴스 상식 조회

    @GetMapping("knowledge")
    public ResponseEntity  readCardnewsKnowledgeList(@RequestParam(name="page", defaultValue="0",required = false)int page,
                                                     @RequestParam(name="limit", defaultValue = "10", required=false)int limit){

        System.out.println("#######     api/admin/cardnews/knowledge   GET #######");

        return new ResponseEntity(adminCardnewsService.readCardnewsKnowledgeList(page,limit), HttpStatus.OK);
    }

    //카드뉴스 컨텐츠 상세보기
    @GetMapping("{cardnewsId}/contents")
    public ResponseEntity readAllCardnewsContentsList(@PathVariable("cardnewsId")int cardnewsId,
                                                      @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                      @RequestParam(name="limit",defaultValue = "10", required=false)int limit){

        System.out.println("#######     api/admin/cardnews/contents   GET #######");

        return new ResponseEntity(adminCardnewsService.readAllCardnewsContentsList(cardnewsId),HttpStatus.OK);
    }


    @GetMapping("list")
    @ResponseBody
    public List<Cardnews> readCardnews() {
        System.out.println("#######     api/admin/cardnews/list   GET #######");

        return adminCardnewsService.readAllCardnews();
    }


    // 푸시 알람 넣어야 한다.
    //카드뉴스 대분류 작성
    @PostMapping
    public ResponseEntity createCardnews(Cardnews cardnews){

        System.out.println("#######     api/admin/cardnews   POST #######");

        System.out.println(1);
        System.out.println(cardnews.getTitle());
        System.out.println(cardnews.getId());
        return new ResponseEntity(adminCardnewsService.createCardnewsService(cardnews), HttpStatus.CREATED);
    }

    //카드뉴스 대분류 수정
    @PutMapping("{cardnewsId}")
    public ResponseEntity updateCardnews(Cardnews cardnews, @PathVariable("cardnewsId")int cardnewsId) {

        System.out.println("#######     api/admin/cardnews/:cardnewsId   PUT #######");

        return new ResponseEntity(adminCardnewsService.updateCardnewsById(cardnews, cardnewsId), HttpStatus.OK);
    }

    //카드뉴스 대분류 삭제
    @DeleteMapping("{cardnewsId}")
    public ResponseEntity deleteCardnews(@PathVariable("cardnewsId")int cardnewsId){

        System.out.println("#######     api/admin/cardnews/:cardnewsId   DELETE #######");

        return new ResponseEntity((adminCardnewsService.deleteCardnewsById(cardnewsId)),HttpStatus.OK);
    }

    //카드뉴스 컨텐츠 작성
    @PostMapping("{cardnewsId}/contents")
    public ResponseEntity createCardnewsContennts(CardnewsContents cardnewsContents, @PathVariable("cardnewsId")int cardnewsId){
        System.out.println("#######     api/admin/cardnews/:cardnewsId/contents   POST #######");

        return new ResponseEntity(adminCardnewsService.createCardnewsContentsService(cardnewsContents,cardnewsId), HttpStatus.CREATED);
    }

    //카드뉴스 컨텐츠 수정
    @PostMapping("contents/{contentsId}")
    public ResponseEntity updateCardnewsContents(CardnewsContents cardnewsContents, @PathVariable("contentsId")int contentsId){
        System.out.println("#######     api/admin/cardnews/contents/:contentsId   POST #######");

        return new ResponseEntity(adminCardnewsService.updateCardnewsContentsById(cardnewsContents, contentsId), HttpStatus.OK);
    }

    @DeleteMapping("contents/{contentsId}")
    public ResponseEntity deleteCardnewsContents(@PathVariable("contentsId")int contentsId){
        System.out.println("#######     api/admin/cardnews/contents/:contentsId   DELETE #######");

        return new ResponseEntity(adminCardnewsService.deleteCardnewsContentsById(contentsId), HttpStatus.OK);
    }




}