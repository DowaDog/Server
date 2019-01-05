package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardnewsContentsDto {

    private int id;
    private String title;
    private String thumnailImg;
    private String detail;
    private boolean auth = false;
}
