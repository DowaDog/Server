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
public class SignupFormDto {

    private String id;
    private String password;
    private String name;
    private String birth;
    private String phone;
    private String email;
    private String gender;
    private String deviceToken;
    private String type="NORMAL";
    private boolean pushAllow=true;
    private MultipartFile profileImgFile;
}
