package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {

    private String phone;
    private String email;
    private String address;
    private String job;
    private boolean havePet;
    private String petInfo;
    private String adoptType="adopt"; // adopt, temp
    private String tempPeriod;
    private String regStatus; // deny, step0, step1, step2, complete
    private String meetPlace;
    private String meetTime;
    private String meetMaterial;
    private boolean validReg = true; // true 진행중, false 종료
    private boolean userCheck = true; //true 유저가 확인함, false 유저가 확인 안함
    private String regType; // 직접방문, 온라인인지 -> 코드테이블 추가해야됨
    private int animalId;


}
