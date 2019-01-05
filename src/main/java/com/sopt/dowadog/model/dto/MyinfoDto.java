package com.sopt.dowadog.model.dto;


import lombok.*;
import org.apache.catalina.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyinfoDto {
    private String userName;
    private String profileImg;
    private int userLike;
    private int userScrap;
    private int userCommunity;
    private String animalName; //null 을 넘겨주면
    private boolean mailboxUpdated;
}

