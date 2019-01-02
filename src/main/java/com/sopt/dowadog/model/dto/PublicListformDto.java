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
public class PublicListformDto {

    //리스트 폼 dto

    private int id;
    private String noticeNo;
    private String type;
    private String sexCd;
    private String kindCd;
    private String region;
    private LocalDate noticeEddt;
    //private boolean liked;
    private boolean remainDateState;
    private String thumbnailImg;
}
