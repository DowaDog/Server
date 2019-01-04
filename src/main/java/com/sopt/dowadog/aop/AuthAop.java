package com.sopt.dowadog.aop;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.JwtService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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


    public AuthAop(UserRepository userRepository, JwtService jwtService, final HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;

    }



    @Pointcut("execution(* com..service..Service.create*(..))")
    public void create() {}

    @Pointcut("execution(* com..service..*Service.update*(..))")
    public void update() {}

    @Pointcut("execution(* com..service..*Service.delete*(..))")
    public void delete() {}



    //조회인데 로그인 필요한경우 사용
    @Pointcut("@annotation(com.sopt.dowadog.annotation.Auth)")
    public void auth() {
    }


    @Around("auth()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        if(validToken() == false) {
            return RES_RESPONSE_ENTITY;
        }
        return pjp.proceed(pjp.getArgs());
    }


    //권한 기본적으로 필요한 작업들
    @Around("create() || update() || delete()")
    public Object AuthForGuest(final ProceedingJoinPoint pjp) throws Throwable{

        if(validToken() == false) return DEFAULT_RES;
        return pjp.proceed(pjp.getArgs());
    }


    private boolean validToken() throws Exception{ // User의 권한 체크 - false:인증(로그인)되지 않은 사용자, true:인증된 사용자
        final String jwt = httpServletRequest.getHeader("Authorization");

        if(!Optional.ofNullable(jwt).isPresent()){
            return false;
        }

        final String userId = jwtService.decode(jwt);

        if(!Optional.ofNullable(userId).isPresent()){
            return false;
        }

        if(!userRepository.findById(userId).isPresent()) return false;

        return true;
    }


}

