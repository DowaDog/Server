package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserCardnewsScrapRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.normal.CardnewsContentsService;
import com.sopt.dowadog.service.normal.CardnewsService;
import com.sopt.dowadog.service.normal.UserService;
import com.sopt.dowadog.service.common.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/normal/cardnews")
@Controller
public class CardnewsController {

    private final static String AUTHORIZATION = "Authorization";

    private final JwtService jwtService;

    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    public CardnewsController(JwtService jwtService, HttpServletRequest httpServletRequest, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
        this.userRepository = userRepository;
    }

    @Autowired
    UserService userService;

    @Autowired
    CardnewsService cardnewsService;

    @Autowired
    CardnewsContentsService cardnewsContentsService;

    @Autowired
    UserCardnewsScrapRepository userCardnewsScrapRepository;

    @GetMapping("education")
    public ResponseEntity readCardnewsEducationList(@RequestHeader(value = "Authorization", required = false)final String jwtToken){

        System.out.println("#######     api/normal/cardnews/education   GET #######");

        //todo 교육 이수 완료 정보 줘야댐
        try{
            User user = userService.getUserByJwtToken(jwtToken);


            return new ResponseEntity(cardnewsService.readCardnewsEducationList(user), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("knowledge")
    public ResponseEntity  readCardnewsKnowledgeList(@RequestHeader(value="Authorization", required = false) final String jwtToken,
                                                     @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                     @RequestParam(name="limit", defaultValue = "10", required=false)int limit){
        System.out.println("#######     api/normal/cardnews/knowledge   GET #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);


            return new ResponseEntity(cardnewsService.readCardnewsKnowledgeList(user,page,limit), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("{cardnewsId}/contents")
    public ResponseEntity readAllCardnewsContentsList(@RequestHeader(value="Authorization", required = false) final String jwtToken,
                                                      @PathVariable("cardnewsId")int cardnewsId,
                                                      @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                      @RequestParam(name="limit",defaultValue = "10", required=false)int limit){

        System.out.println("#######     api/normal/cardnews/:cardnewsId/contents   GET #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);

            if(user == null){
                return new ResponseEntity(cardnewsContentsService.readAllCardnewsContentsList(user, cardnewsId), HttpStatus.OK);
            }else{
                user = userService.getUserByJwtToken(jwtToken);
                return new ResponseEntity(cardnewsContentsService.readAllCardnewsContentsList(user, cardnewsId), HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{cardnewsId}/complete")
    public ResponseEntity createComplete(@RequestHeader(value="Authorization", required = false) final String jwtToken,
                                         @PathVariable("cardnewsId")int cardnewsId){

        System.out.println("#######     api/normal/cardnews/:cardnewsId/complete   POST #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(cardnewsService.createCardnewsEducated(user,cardnewsId),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{cardnewsId}/scrap")
    public ResponseEntity createscrap(@RequestHeader(value="Authorization", required = false) final String jwtToken,
                                      @PathVariable("cardnewsId")int cardnewsId){

        System.out.println("#######     api/normal/cardnews/:cardnewsId/scrap   POST #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            if(user != null) {
                return new ResponseEntity(cardnewsService.createCardnewsScrap(user, cardnewsId), HttpStatus.OK);
            }else{
                return new ResponseEntity(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
