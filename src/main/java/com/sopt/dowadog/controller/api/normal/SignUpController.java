package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.SignupFormDto;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.normal.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        System.out.println("#######     api/signup/duplicateId   GET #######");

        return new ResponseEntity(signUpService.duplicateUserId(id), HttpStatus.OK);
    }

    //Email 중복체크
    @GetMapping("duplicateEmail")
    public ResponseEntity duplicateUserEmail(@RequestParam String email){

        System.out.println("#######     api/signup/duplicateEmail   GET #######");

        return new ResponseEntity(signUpService.duplicateUserEmail(email), HttpStatus.OK);
    }

    //회원가입
    @PostMapping
    public ResponseEntity signup(SignupFormDto signupFormDto, @RequestPart(value="profileImgFile", required=false) final MultipartFile profileImgFile) {

        System.out.println("#######     api/signup   POST #######");


        System.out.println(signupFormDto.toString());

        if(profileImgFile!= null){
            System.out.println(profileImgFile.getOriginalFilename());

            System.out.println("MULTIPART FILE 들어왔습니다");
            System.out.println("사이즈는");
            System.out.println(profileImgFile.getSize());
            System.out.println("파일이름은");
            System.out.println(profileImgFile.getOriginalFilename());
            System.out.println("컨텐트타입은");
            System.out.println(profileImgFile.getContentType());

        }

        return new ResponseEntity(signUpService.newUser(signupFormDto, profileImgFile), HttpStatus.OK);
    }

}
