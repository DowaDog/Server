//package com.sopt.dowadog.controller.api;
//
//import com.sopt.dowadog.model.common.DefaultRes;
//import com.sopt.dowadog.model.common.LoginReq;
//import com.sopt.dowadog.service.AuthService;
//import com.sopt.dowadog.util.ResponseMessage;
//import com.sopt.dowadog.util.StatusCode;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Slf4j
//@Controller
//@RequestMapping("api/login")
//public class LoginController {
//
//
//    private static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
//
//    private final AuthService authService;
//
//    public LoginController(final AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping
//    public ResponseEntity login(@RequestBody final LoginReq loginReq) {
//        try {
//            return new ResponseEntity<>(authService.loginCheck(loginReq), HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
