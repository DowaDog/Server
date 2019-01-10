package com.sopt.dowadog.aop;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.service.common.JwtService;
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
public class CareAuthAop {

    private final CareRepository careRepository;

    private final JwtService jwtService;

    //실패 시 기본 반환 Response
    private final static DefaultRes DEFAULT_RES = DefaultRes.builder().status(401).message("인증 실패").build();
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;


    public CareAuthAop(CareRepository careRepository, JwtService jwtService, final HttpServletRequest httpServletRequest) {
        this.careRepository = careRepository;
        this.jwtService = jwtService;
        this.httpServletRequest = httpServletRequest;

    }

    @Pointcut("execution(* com..service.care.*Service.read*(..))")
    public void select() {}

    @Pointcut("execution(* com..service.care.*Service.create*(..))")
    public void create() {}

    @Pointcut("execution(* com..service.care.*Service.update*(..))")
    public void update() {}

    @Pointcut("execution(* com..service.care.*Service.delete*(..))")
    public void delete() {}


    //권한 기본적으로 필요한 작업들
    @Around("select() || create() || update() || delete()")
    public Object AuthForGuest(final ProceedingJoinPoint pjp) throws Throwable{

        if(validToken() == false) return DEFAULT_RES;
        return pjp.proceed(pjp.getArgs());
    }


    private boolean validToken() throws Exception
    { // User의 권한 체크 - false:인증(로그인)되지 않은 사용자, true:인증된 사용자
        System.out.println("========= AOP ==========");

        System.out.println("보호소 사용자 검증");
        final String jwt = httpServletRequest.getHeader("Authorization");

        if(!Optional.ofNullable(jwt).isPresent()){
            System.out.println("jwt토큰 미존재");
            return false;
        }

        final String userId = jwtService.decode(jwt);

        if(!Optional.ofNullable(userId).isPresent()){
            System.out.println("jwt토큰 유효하지 않음");
            return false;
        }

        if(!careRepository.findByCareUserId(userId).isPresent()) {
            System.out.println("해당 유저 존재하지 않음");
            return false;
        }

        System.out.println("검증완료");

        return true;
    }


}
