package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean liked;
    private Integer remailNoticeDate;
}


