package com.sopt.dowadog.enumeration;

import lombok.Getter;

@Getter
public enum PublicAnimalTypeEnum {

    dog("개"), cat("고양이"), etc("기타");

    private final String value;

    PublicAnimalTypeEnum(String value) {
        this.value = value;
    }


}

