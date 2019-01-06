package com.sopt.dowadog.enumeration;

import lombok.Getter;

@Getter
public enum MailboxTypeEnum {
    content("https://ryudd.s3.amazonaws.com/dowadog/mailbox/post_app.png"), registration( "https://ryudd.s3.amazonaws.com/dowadog/mailbox/post_document.png"), photo("https://ryudd.s3.amazonaws.com/dowadog/mailbox/post_photo.png"), medical("https://ryudd.s3.amazonaws.com/dowadog/mailbox/post_medical.png");

    private final String value;

    MailboxTypeEnum(String value) {
        this.value = value;
    }

}
