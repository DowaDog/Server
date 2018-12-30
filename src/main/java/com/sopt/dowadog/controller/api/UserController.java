package com.sopt.dowadog.controller.api;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity createUser(User user) {
        System.out.println(user.toString());
        return new ResponseEntity(userService.createUser(user), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity readAllUserList() {
        return new ResponseEntity(userService.readUserList(), HttpStatus.OK);
    }
}
