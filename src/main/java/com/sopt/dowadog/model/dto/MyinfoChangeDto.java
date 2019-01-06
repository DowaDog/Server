package com.sopt.dowadog.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyinfoChangeDto {
    private String name;
    private String thumbnailImg;
    private String birth;
    private String phone;

}