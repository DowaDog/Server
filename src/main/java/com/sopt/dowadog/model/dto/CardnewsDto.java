package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardnewsDto {

    private int id;
    private String title;
    private String subtitle;
    private String type;
    private String imgPath;
    private boolean educated = false;
    private boolean auth = false;
    private boolean scrap = false;


}
