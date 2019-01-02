package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicAnimalDetailDto {

    private int id;
    private String noticeNo;
    private String type;
    private String processState;
    private String sexCd;
    private LocalDate noticeStdt;
    private LocalDate noticeEddt;
    private String region;
    private String specialMark;
    private boolean remainDateState;
    private String kindCd;
    private String age;
    private String weight;
    private String thumbnailImg;
    private String happenPlace;
    private String careName;
    private String careTel;

    // 좋아요에 대해서는 나중에 구현

}
