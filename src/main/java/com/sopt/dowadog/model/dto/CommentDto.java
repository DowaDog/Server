package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private int id;
    private String detail;
    private String userId;
    private String userProfileImg;
    private boolean today;
    private Date createdAt;
    private Date updatedAt;
    private boolean auth = false;


}
