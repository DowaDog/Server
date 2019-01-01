package com.sopt.dowadog.aop;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.JwtService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthAop {

    private final static String AUTHORIZATION = "Authorization";

    private final UserRepository userRepository;

    private final JwtService jwtService;

    //실패 시 기본 반환 Response
    private final static DefaultRes DEFAULT_RES = DefaultRes.builder().status(401).message("인증 실패").build();
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;
//
//    private final UserMapper userMapper;
//
//    private final JwtService jwtService;


    public AuthAop(UserRepository userRepository, JwtService jwtService, final HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;
//        this.userMapper = userMapper;
//        this.jwtService = jwtService;
    }


    @Pointcut("@annotation(com.sopt.dowadog.annotation.Auth)")
    public void auth() {
        // pointcut annotation 값을 참조하기 위한 dummy method
    }

    @Around("auth()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {

        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);
        //httpServletRequest.setAttribute("type", token.);

        if (jwt == null) return RES_RESPONSE_ENTITY;
        //토큰 해독
        final String userId = jwtService.decode(jwt);
        //토큰 검사
        if (userId == null) {
            return RES_RESPONSE_ENTITY;
        } else {
            //todo User정보 가져와서 처리해야함

            User user = userRepository.getOne(userId);
            //유효 사용자 검사
            if (user == null) return RES_RESPONSE_ENTITY;

            return pjp.proceed(pjp.getArgs());
        }
    }


}

