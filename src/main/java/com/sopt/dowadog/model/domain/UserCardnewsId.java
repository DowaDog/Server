package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class UserCardnewsId implements Serializable {

    private String user;
    private Integer cardnews;

}
