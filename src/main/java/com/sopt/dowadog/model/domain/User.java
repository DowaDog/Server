package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateEntity {

    @Id
    private String id;

    private String password;
    private String nickname;
    private String name;
    private String birth;
    private String phone;
    private String email;
    private String gender;
    private String deviceToken;
    private String type;
    private String profileImg;
    private boolean pushAllow;


    @Transient
    @JsonIgnore
    private MultipartFile profileImgFile;

}

