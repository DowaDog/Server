package com.sopt.dowadog.service.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sopt.dowadog.enumeration.JwtExpireTermEnum;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.common.JwtToken;
import com.sopt.dowadog.model.common.LoginReq;
import com.sopt.dowadog.model.common.Token;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    @Value("${JWT.ISSUER}")
    private String ISSUER;

    @Value("${JWT.SECRET}")
    private String SECRET;

    @Autowired
    AuthService authService;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public DefaultRes generateToken(LoginReq loginReq, String type) {
        if (authService.loginCheck(loginReq, type)) {
            long nowTime = System.currentTimeMillis() / 1000;

            Token accessToken = createToken(loginReq.getId(), JwtExpireTermEnum.ACCESS, nowTime);
            Token refreshToken = createToken(loginReq.getId(), JwtExpireTermEnum.REFRESH, nowTime);
            JwtToken jwtToken = new JwtToken(accessToken, refreshToken);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LOGIN_SUCCESS, jwtToken);

        } else {
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
        }
    }

    public DefaultRes renewAccessToken(String refreshToken, String type) { //리프레시 토큰이용해서 액세스 토큰 갱신-> 유저용 이거 케어 부분은 다르게 다르게 해줘야 함
        long nowTime = System.currentTimeMillis() / 1000;


        try {
            String userId = decode(refreshToken);

            if (userId == null) DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.AUTH_FAIL);

            if (authService.idCheck(userId, type)) {
                System.out.println(type + "리프레시 토큰 발급");
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.AUTH_SUCCESS, createToken(userId, JwtExpireTermEnum.ACCESS, nowTime));
            } else {
                return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.AUTH_FAIL);
            }
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.AUTH_FAIL);

        }
    }


    private Token createToken(String userId, JwtExpireTermEnum type, long nowTime) {

        long expireTime = nowTime + type.getExpireTerm();


        Date expireDate = new Date(expireTime * 1000);

        String data = JWT.create()
                .withExpiresAt(expireDate)
                .withIssuer(ISSUER)
                .withClaim("user_id", userId)
                .sign(Algorithm.HMAC256(SECRET));


        return new Token(data, nowTime, expireTime);

    }


    public String decode(String token) {
        //todo 예외부분 throw 해서 호출부분( AOP 에서 처리하도록 변경 )
        //토큰 해독 객체 생성

        try {
            final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            //토큰 검증
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            //토큰 payload 반환, 정상적인 토큰이라면 토큰 주인(사용자) 고유 ID, 아니라면 null

            return decodedJWT.getClaim("user_id").asString();
        } catch (Exception e){
            return null;
        }

    }


}
