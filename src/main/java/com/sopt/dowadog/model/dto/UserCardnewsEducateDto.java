package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardnewsEducateDto {
    private int cardnewsId;
    private String userId;
    private int allCardnews;
    private int allEducated;
    private boolean allComplete;
}
