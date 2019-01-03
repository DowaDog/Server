package com.sopt.dowadog.model.dto;

import lombok.Data;


@Data
public class PublicAnimalSearchDto {

    private String type;
    private Integer remainNoticeDate;
    private String region;
    private String searchWord;
}
