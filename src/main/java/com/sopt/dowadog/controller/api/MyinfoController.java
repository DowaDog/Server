package com.sopt.dowadog.controller.api;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.MyinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/mypage")  //URL Mapping은 myinfo 라는 의미이다.
@Controller
public class MyinfoController {

    @Autowired //myinfoService 객체에 대해 component 애너 붙은 클래스 중 해당 타입의 클래스를 찾아 객체 주입
    MyinfoService myinfoService;




    //마이페이지 조회 :
   @GetMapping
   public ResponseEntity readMyinfo(String userId) {
      return new ResponseEntity(myinfoService.readMypageByUserId(userId), HttpStatus.OK);
   }

    //동물 정보 수정
   // @PutMapping("animals")
    //public ResponseEntity updateanimalsinfo(Object object){
    //    return new ResponseEntity(myinfoService.updateAnimalByUserId(), HttpStatus.OK);
    //}

    //사람 정보 수정
    @PutMapping("user")
    public ResponseEntity updateuserinfo(User user , String userId){

       return new ResponseEntity(myinfoService.updateUserInfoByUserId(user, userId),HttpStatus.OK);
    }

    //좋아요 리스트 조회
    @GetMapping("/likes/animals")
    public ResponseEntity readMypageLikes
    (@RequestParam(name = "page", defaultValue = "0", required = false) int page,
     @RequestParam(name = "limit", defaultValue = "10", required = false) int limit ,String userId) {
       return new ResponseEntity(myinfoService.readLikeListByUserId(page, limit, userId), HttpStatus.OK);
    }

    //스크랩 리스트 조회
    @GetMapping("/clips")
    public ResponseEntity readMypageClips
    (@RequestParam(name = "page", defaultValue = "0", required = false) int page,
     @RequestParam(name = "limit", defaultValue = "10", required = false) int limit , String userId) {

        return new ResponseEntity(myinfoService.readClipsListByUserId(page, limit, userId), HttpStatus.OK);
    }

    //내가 쓴글 리스트 조회
    @GetMapping("/community")
    public ResponseEntity readMypageCommunity
    (@RequestParam(name = "page", defaultValue = "0", required = false) int page,
     @RequestParam(name = "limit", defaultValue = "10", required = false) int limit ,String userId) {

       return new ResponseEntity(myinfoService.readCommunityListByUserId(page, limit, userId), HttpStatus.OK);
    }


    //우체통 조회
    @GetMapping("/mailboxes")
    public ResponseEntity readMypageMailboxes(String userId) {
        return new ResponseEntity(myinfoService.readMailboxesUserId(userId), HttpStatus.OK);
        //return new ResponseEntity(HttpStatus.OK);
    }
}
//컨트롤러는 클라이언트에게 보여줄 뷰
