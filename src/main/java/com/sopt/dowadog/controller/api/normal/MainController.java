package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.normal.MainService;
import com.sopt.dowadog.service.normal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/normal/main")
@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    MainService mainService;

    @GetMapping
    public ResponseEntity main(@RequestHeader(value = "Authorization", required = false) final String jwtToken) {
        System.out.println("#######     api/normal/main   GET #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(mainService.readMain(user), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("check")
    public ResponseEntity mainCheck(@RequestHeader(value = "Authorization", required = false) final String jwtToken){
        System.out.println("#######     api/normal/main   GET #######");


        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(mainService.checkMain(user), HttpStatus.OK);


        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
