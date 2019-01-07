package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.RegistrationDto;
import com.sopt.dowadog.service.normal.RegistrationService;
import com.sopt.dowadog.service.normal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/normal/registrations")
@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;


    @PostMapping("online")
    public ResponseEntity online(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                @RequestBody RegistrationDto registrationDto) {

        System.out.println("#######     api/normal/registrations/online   POST #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);
            System.out.println("온라인 신청서 사용자 검증완료");
            return new ResponseEntity(registrationService.createRegistration(user, registrationDto, "online"), HttpStatus.CREATED);
            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("offline")
    public ResponseEntity offline(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                 @RequestBody RegistrationDto registrationDto) {

        System.out.println("#######     api/normal/registrations/offline   POST #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);
            System.out.println("오프라인 신청서 사용자 검증완료");
            return new ResponseEntity(registrationService.createRegistration(user, registrationDto, "offline"), HttpStatus.CREATED);
            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("check")
    public ResponseEntity check(@RequestHeader(value = "Authorization", required = false) final String jwtToken) {

        System.out.println("#######     api/normal/registrations/check   POST #######");

        try{
            User user = userService.getUserByJwtToken(jwtToken);

            if(user == null) return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity(userService.checkRegistration(user), HttpStatus.OK);
            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }



}
