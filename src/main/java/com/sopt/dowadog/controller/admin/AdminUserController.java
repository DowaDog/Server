package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/user")
public class AdminUserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity readAllUserList() {
        return new ResponseEntity(userService.readUserList(), HttpStatus.OK);
    }
}
