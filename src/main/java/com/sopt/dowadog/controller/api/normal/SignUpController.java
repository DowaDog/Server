package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/signup")
public class SignUpController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SignUpService signUpService;

    //Id 중복체크
    @GetMapping("duplicateId")
    public ResponseEntity duplicateUserId(@RequestParam String id){

        return new ResponseEntity(signUpService.duplicateUserId(id), HttpStatus.OK);
    }

    //Email 중복체크
    @GetMapping("duplicateEmail")
    public ResponseEntity duplicateUserEmail(@RequestParam String email){

        return new ResponseEntity(signUpService.duplicateUserEmail(email), HttpStatus.OK);
    }

    //회원가입
    @PostMapping
    public ResponseEntity createUser(User user) {

        return new ResponseEntity(signUpService.newUser(user), HttpStatus.OK);
    }

}
