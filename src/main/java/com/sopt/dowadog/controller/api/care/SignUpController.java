package com.sopt.dowadog.controller.api.care;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.service.care.SignUpService;
import com.sopt.dowadog.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

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


        try{

            return new ResponseEntity(signUpService.createCare(care), HttpStatus.OK);

        }catch (Exception e){

            return new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //보호소 아이디 중복확인
    @GetMapping("/duplicateId")
    public ResponseEntity readCareId(@RequestParam(name = "id") final String careId){
        try{

            return new ResponseEntity(signUpService.readCareId(careId),HttpStatus.OK);

        }catch (Exception e){

            return  new ResponseEntity(DefaultRes.FAIL_DEFAULT_RES,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
