package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.annotation.Auth;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.AnimalUserAdopt;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.MyinfoService;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/normal/mypage")  //URL Mapping은 myinfo 라는 의미이다.
@Controller
public class MyinfoController {

    @Autowired //myinfoService 객체에 대해 component 애너 붙은 클래스 중 해당 타입의 클래스를 찾아 객체 주입
     MyinfoService myinfoService;

    @Autowired
    UserService userService;


    //마이페이지 조회
    @GetMapping
    public ResponseEntity readMyinfo(@RequestHeader(value = "Authorization", required = false) String jwtToken) {
        User user = userService.getUserByJwtToken(jwtToken);
        return new ResponseEntity(myinfoService.readMypage(user), HttpStatus.OK);
    }

    //사람 정보 수정
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken, User modifiedUser) {
        User user = userService.getUserByJwtToken(jwtToken);
        return new ResponseEntity(myinfoService.updateUserInfo(user, modifiedUser), HttpStatus.OK);
    }

    //내 입양동물 리스트 정보 조회
    @GetMapping("adoptAnimals")
    public ResponseEntity readMyAdoptAnimal(@RequestHeader(value = "Authorization", required = false) String jwtToken) {
        User user = userService.getUserByJwtToken(jwtToken);
        return new ResponseEntity(myinfoService.readAnimalUserAdoptList(user), HttpStatus.OK);
    }


    //todo 인증 예외처리, 예방접종 코드테이블도 같이해서 보내줘야함 ( DTO 필요 )
    @Auth
    @GetMapping("adoptAnimals/{adoptAnimalId}")
    public ResponseEntity readAnimalInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                         @PathVariable(name = "adoptAnimalId") int adoptAnimalId) {


        User user = userService.getUserByJwtToken(jwtToken);
        return new ResponseEntity(myinfoService.readAnimalUserAdoptById(user, adoptAnimalId), HttpStatus.OK);

    }

    //동물 정보 수정 //todo codetable로 접종여부도 가져와야함
    @PutMapping("adoptAnimals/{adoptAnimalId}")
    public ResponseEntity updateAnimalInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                           @RequestBody AnimalUserAdopt animalUserAdopt,
                                           @PathVariable(name = "adoptAnimalId") int adoptAnimalId) {



        try {

            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.updateAnimalByAnimalId(user, animalUserAdopt, adoptAnimalId), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //좋아요 리스트 조회
    @GetMapping("/likes/animals")
    public ResponseEntity readMypageLikes
    (@RequestHeader(value = "Authorization", required = false) String jwtToken) {
        User user = userService.getUserByJwtToken(jwtToken);

        return new ResponseEntity(myinfoService.readMyLikeList(user), HttpStatus.OK);
    }

    //스크랩 리스트 조회
    @GetMapping("/clips")
    public ResponseEntity readMypageClips
    (@RequestParam(name = "page", defaultValue = "0", required = false) int page,
     @RequestParam(name = "limit", defaultValue = "10", required = false) int limit, String userId) {

        return new ResponseEntity(myinfoService.readClipsListByUserId(page, limit, userId), HttpStatus.OK);
    }

    //내가 쓴글 리스트 조회
    @GetMapping("/community")
    public ResponseEntity readMypageCommunity
    (@RequestParam(name = "page", defaultValue = "0", required = false) int page,
     @RequestParam(name = "limit", defaultValue = "10", required = false) int limit, String userId) {

        return new ResponseEntity(myinfoService.readCommunityListByUserId(page, limit, userId), HttpStatus.OK);
    }


//    //우체통 조회
//    @GetMapping("/mailboxes")
//    public ResponseEntity readMypageMailboxes(String userId) {
//        return new ResponseEntity(myinfoService.readMailboxesUserId(userId), HttpStatus.OK);
//        //return new ResponseEntity(HttpStatus.OK);
//    }
}
//컨트롤러는 클라이언트에게 보여줄 뷰
