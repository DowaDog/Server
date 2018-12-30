package com.sopt.dowadog.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyinfoDto {
    private String userName;
    private int userLike;
    private int userScrap;
    private int userCommunity;
    private String animalName; //null 을 넘겨주면
    private boolean mailboxUpdated;
}

