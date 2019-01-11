package com.sopt.dowadog.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardnewsContentsListDto {
    private String cardnewsThumbnail;
    List<CardnewsContentsDto> content;
    AllEducatedDto edu;
}
