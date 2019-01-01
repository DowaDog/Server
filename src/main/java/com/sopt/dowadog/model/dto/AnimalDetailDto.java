package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDetailDto {

    private int id;
    private String type;
    private String processState;
    private String sexCd;
    private LocalDate noticeStdt;
    private LocalDate noticeEddt;
    private String region;
    private boolean remainDateState;
    private String kindCd;
    private String age;
    private String weight;
    private String thumbnailImg;
    private String happenPlace;
    private String careName;
    private String careTel;
    private boolean liked;
    private List<String> animalStoryList;

}
