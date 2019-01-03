package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListformDto {

    private int id;
    private String type;
    private String sexCd;
    private String kindCd;
    private String region;
    private LocalDate noticeEddt;
    private boolean liked;
    private boolean remainDateState;
    private String thumbnailImg;
    private String processState;
}


