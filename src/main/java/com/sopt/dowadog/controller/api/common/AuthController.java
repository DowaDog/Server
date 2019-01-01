package com.sopt.dowadog.controller.api.common;

import com.sopt.dowadog.model.common.LoginReq;
import com.sopt.dowadog.service.AuthService;
import com.sopt.dowadog.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("api/auth")
@Controller
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @PostMapping("login") // 로그인은 토큰을 발급받는 수단 - JwtToken(AccessToken, RefreshToken 가짐)을 리턴.
    public ResponseEntity login(@RequestBody final LoginReq loginReq) {
        return new ResponseEntity(jwtService.createJwtToken(loginReq), HttpStatus.OK);
    }

    @PostMapping("refresh") // 리프레시 토큰값을 통해 AccessToken 갱신
    public ResponseEntity refreshToken(@RequestHeader("Authorization") String refreshTokenData) {
        return new ResponseEntity(jwtService.renewAccessToken(refreshTokenData), HttpStatus.OK);

    }


}
