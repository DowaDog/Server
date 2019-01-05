package com.sopt.dowadog.enumeration;

import lombok.Getter;

@Getter
public enum MailboxTypeEnum {
    CONTENT("content", "content.jpg"), REGISTRATION("registration", "registration.jpg"), PHOTO("photo", "photo.jpg"), MEDICAL("medical", "medical.jpg");

    private final String value;
    private final String key;

    MailboxTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
