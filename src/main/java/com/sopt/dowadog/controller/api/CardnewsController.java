package com.sopt.dowadog.controller.api;

import com.sopt.dowadog.annotation.Auth;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.CardnewsContentsService;
import com.sopt.dowadog.service.CardnewsService;
import com.sopt.dowadog.service.JwtService;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/cardnews")
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

    @GetMapping("/education")
    public ResponseEntity readCardnewsEducationList(@RequestParam("type")String type){

        //todo 교육 이수 완료 정보 줘야함

        return new ResponseEntity(cardnewsService.readCardnewsList(type), HttpStatus.OK);
    }

    @GetMapping("/knowledge")
    public ResponseEntity  readCardnewsKnowledgeList(@RequestParam("type")String type,
                                                     @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                     @RequestParam(name="limit", defaultValue = "10", required=false)int limit){

        return new ResponseEntity(cardnewsService.readCardnewsList(type), HttpStatus.OK);
    }


    @GetMapping("{cardnewsId}/contents")
    public ResponseEntity readAllCardnewsContentsList(@PathVariable("cardnewsId")int cardnewsId,
                                                      @RequestParam(name="page", defaultValue="0",required = false)int page,
                                                      @RequestParam(name="limit",defaultValue = "10", required=false)int limit){

        return new ResponseEntity(cardnewsContentsService.readAllCardnewsContentsList(cardnewsId),HttpStatus.OK);
    }

    @PostMapping("/cardnews/{cardnewsId}/complete")
    public ResponseEntity createComplete(@PathVariable("cardnesId")int cardnewsId){
        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);

        final JwtService.Token token = jwtService.decode(jwt);

        //todo User 정보에서 type 가져와야함
        User user = new User();
        user.setId("Rdd");

        //todo userService에서 type받아서 educatedCardnews List에 채워서 반환하는 메소드 만들어야댐
        //     지금은 빈메소드인 상태!!
        return new ResponseEntity(userService.a(), HttpStatus.OK);
    }

}
