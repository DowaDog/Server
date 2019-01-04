package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.RegistrationService;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/normal/registration")
@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;


    @PostMapping
    public ResponseEntity apply(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                @RequestBody Registration registration) {
        try{
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(registrationService.createRegistration(user, registration), HttpStatus.CREATED);
            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }


}
