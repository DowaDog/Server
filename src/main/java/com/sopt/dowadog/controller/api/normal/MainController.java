package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("api/normal/main")
@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
//    @Autowired
//    MainS

    @GetMapping
    public ResponseEntity main(@RequestHeader(value = "Authorization", required = false) final String jwtToken) {

        try {
            User user = null;

            if (userService.getUserByJwtToken(jwtToken) != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }
            return null;
//            return new ResponseEntity(communityService.readCommunityById(user, communityId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
