package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.annotation.Auth;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.AnimalUserAdopt;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.MailboxDto;
import com.sopt.dowadog.model.dto.MyinfoChangeDto;
import com.sopt.dowadog.model.dto.SignupFormDto;
import com.sopt.dowadog.service.normal.MyinfoService;
import com.sopt.dowadog.service.normal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        System.out.println("#######     api/normal/mypage   GET #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readMypage(user), HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    //사람 정보 수정
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                         MyinfoChangeDto myinfoChangeDto,
                                         @RequestPart(value="profileImgFile", required=false) final MultipartFile profileImgFile) {

        System.out.println("#######     api/normal/mypage   PUT #######");
        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.updateUserInfo(user, myinfoChangeDto, profileImgFile), HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();

            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    //내 입양동물 리스트 정보 조회
    @GetMapping("adoptAnimals")
    public ResponseEntity readMyAdoptAnimal(@RequestHeader(value = "Authorization", required = false) String jwtToken) {

        System.out.println("#######     api/normal/mypage/adoptAnimals   GET #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readAnimalUserAdoptList(user), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }


    //todo 인증 예외처리, 예방접종 코드테이블도 같이해서 보내줘야함 ( DTO 필요 )
    @GetMapping("adoptAnimals/{adoptAnimalId}")
    public ResponseEntity readAnimalInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                         @PathVariable(name = "adoptAnimalId") int adoptAnimalId) {

        System.out.println("#######     api/normal/mypage/adoptAnimals/:adoptAnimalId   GET #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readAnimalUserAdoptById(user, adoptAnimalId), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }

    }

    //동물 정보 수정 //todo codetable로 접종여부도 가져와야함
    @PutMapping("adoptAnimals/{adoptAnimalId}")
    public ResponseEntity updateAnimalInfo(@RequestHeader(value = "Authorization", required = false) String jwtToken,
                                           AnimalUserAdopt animalUserAdopt,
                                           @PathVariable(name = "adoptAnimalId") int adoptAnimalId) {

        System.out.println("#######     api/normal/mypage/adoptAnimals/:adoptAnimalId   PUT #######");

        try {

            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.updateAnimalByAnimalId(user, animalUserAdopt, adoptAnimalId), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }


    //좋아요 리스트 조회 완료
    @GetMapping("/likes")
    public ResponseEntity readMypageLikes
    (@RequestHeader(value = "Authorization", required = false) String jwtToken) {

        System.out.println("#######     api/normal/mypage/likes   GET #######");

        try {

            User user = userService.getUserByJwtToken(jwtToken);

            return new ResponseEntity(myinfoService.readMyLikeList(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    //스크랩 리스트 조회 //todo 수한이 작업 완료하면 그때
    @GetMapping("/scraps")
    public ResponseEntity readMypageClips
    (@RequestHeader(value = "Authorization", required = false) String jwtToken) {
        //todo 나중에 상세 예외처리

        System.out.println("#######     api/normal/mypage/scraps   GET #######");

        try {
            //밑의 함수의 경우 throws를 사용해서 꼭 try catch문을 해야 함!(에러를 최상단에서 처리하기 위해서)
            User user = userService.getUserByJwtToken(jwtToken);

            System.out.println(myinfoService.readMyClipsList(user).getData().get(0).getCreatedAt().getClass().getName());

            return new ResponseEntity(myinfoService.readMyClipsList(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }


    }

    //내가 쓴글 리스트 조회
    @GetMapping("/community")
    public ResponseEntity readMypageCommunity
    (@RequestHeader(value = "Authorization", required = false) String jwtToken) {

        System.out.println("#######     api/normal/mypage/community   GET #######");

        //todo 나중에 상세 예외처리
        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readMyCommunityList(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }


    //우체통 조회
    @GetMapping("/mailboxes")
    public ResponseEntity<MailboxDto> readMypageMailboxes(@RequestHeader(value = "Authorization", required = false) String jwtToken) {

        System.out.println("#######     api/normal/mypage/mailboxes   GET #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readMailboxes(user), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }

        //return new ResponseEntity(HttpStatus.OK);
    }
    //내 정보 조회
    @GetMapping("/myinfo")
    public ResponseEntity<MyinfoChangeDto> readMypageMyinfo(@RequestHeader(value = "Authorization",required = false)final String jwtToken){

        System.out.println("#######     api/normal/mypage/myinfo   GET #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.readMyinfo(user),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //우체통 읽음처리 컨트롤러
    @GetMapping("/mailboxes/readings")
    public ResponseEntity updateMailboxState(@RequestHeader(value = "Authorization",required = false) final String jwtToken){
        System.out.println("#######     api/normal/mypage/mailboxes/readings  GET #######");
        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(myinfoService.updateMailboxesState(user),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
//컨트롤러는 클라이언트에게 보여줄 뷰
