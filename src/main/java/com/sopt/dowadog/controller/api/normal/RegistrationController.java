package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.RegistrationDto;
import com.sopt.dowadog.repository.RegistrationRepository;
import com.sopt.dowadog.service.RegistrationService;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/normal/registration")
@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegistrationRepository registrationRepository;

    @PostMapping("online")
    public ResponseEntity online(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                @RequestBody RegistrationDto registrationDto) {
        try{
            User user = userService.getUserByJwtToken(jwtToken);
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
        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(registrationService.createRegistration(user, registrationDto, "offline"), HttpStatus.CREATED);
            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }



}
