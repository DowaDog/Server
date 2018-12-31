package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
public class HashtagAnimalId implements Serializable {

    private Integer hashtag;
    private Integer animal;
}
