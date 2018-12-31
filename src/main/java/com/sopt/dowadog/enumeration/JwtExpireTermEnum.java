package com.sopt.dowadog.enumeration;

import lombok.Getter;

@Getter
public enum JwtExpireTermEnum {
    //15분, 1시간
    ACCESS(900), REFRESH(3600*24*365);

    private long expireTerm;

    JwtExpireTermEnum(long expireTerm){
        this.expireTerm = expireTerm;
    }
}
