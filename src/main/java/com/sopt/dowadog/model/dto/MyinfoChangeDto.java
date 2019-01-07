package com.sopt.dowadog.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyinfoChangeDto {
    private String name;
    private String birth;
    private String phone;
    private String thumbnailImg;

}