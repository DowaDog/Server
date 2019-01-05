package com.sopt.dowadog.controller.api.care;

import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.service.care.SignUpService;
import com.sopt.dowadog.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("careSignupController")
@RequestMapping("api/care/signup")
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(final SignUpService signUpService){
        this.signUpService = signUpService;

    }





    //보호소 회원가입
    @PostMapping
    public ResponseEntity createCare(@RequestBody Care care){
        System.out.print(care.getAddress());
        System.out.print(care.getTel());


        return new ResponseEntity(signUpService.createCare(care), HttpStatus.OK);

    }


}
