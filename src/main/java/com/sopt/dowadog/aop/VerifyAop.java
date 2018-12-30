package com.sopt.dowadog.aop;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
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
public class VerifyAop {

    private final static String VERIFY = "verify";

    @Autowired
    JwtService jwtService;

    private final static DefaultRes DEFAULT_RES = DefaultRes.builder().status(401).message("인증 실패").build();
    private final static DefaultRes VERIFY_RES = DefaultRes.builder().status(403).message("권한없음").build();
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY1 = new ResponseEntity<>(VERIFY_RES, HttpStatus.FORBIDDEN);

    private final HttpServletRequest httpServletRequest;

    public VerifyAop(final HttpServletRequest httpServletRequest){
        this.httpServletRequest = httpServletRequest;
    }

    @Pointcut("@annotation(com.sopt.dowadog.annotation.Verify)")
    public void verify(){
        //// pointcut annotation 값을 참조하기 위한 dummy method
    }

    @Around("verify()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable{
        final String jwt = httpServletRequest.getHeader(VERIFY);

        //헤더에 값 안들어 있으면 401 던짐
        if(jwt == null) return RES_RESPONSE_ENTITY;

        final JwtService.Token token = jwtService.decode(jwt);

        //token값 없어도 401 던짐
        if(token == null){
            return RES_RESPONSE_ENTITY;
        } else {

            //todo 유저 정보 받아와서 처리하는 부분 들어가야함
            User user = new User();
            user.setId("rdd");
            user.setType("admin");

            if(user == null) {
                return RES_RESPONSE_ENTITY;
            } else {
                if(user.getType() != "admin"){
                    //user type이 admin이 아니면 권한 없음 에러를 튕김
                    return RES_RESPONSE_ENTITY1;
                }
                return pjp.proceed(pjp.getArgs());
            }
        }
    }


}
