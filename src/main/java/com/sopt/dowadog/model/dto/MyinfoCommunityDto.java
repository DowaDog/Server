package com.sopt.dowadog.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyinfoCommunityDto {

    private int id;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}
