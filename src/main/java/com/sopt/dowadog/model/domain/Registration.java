package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.RegistrationDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Registration.class)
public class Registration extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String phone;
    private String email;
    private String address;
    private String job;
    private boolean havePet;
    private String petInfo;
    private String tempProtect="adopt"; // adopt, temp
    private String tempPeriod;
    private String regStatus; // deny, step0, step1, step2, complete
    private String meetPlace;
    private String meetTime;
    private String meetMaterial;
    private boolean validReg = true; // true 진행중, false 종료
    private boolean userCheck = true; //true 유저가 확인함, false 유저가 확인 안함
    private String type; // 직접방문, 온라인인지 -> 코드테이블 추가해야됨

    @ManyToOne
    private User user;

    @ManyToOne
    private Animal animal;



    public static Registration getRegistrationByDto(RegistrationDto registrationDto) {
        return Registration.builder()
                .phone(registrationDto.getPhone())
                .email(registrationDto.getEmail())
                .address(registrationDto.getAddress())
                .job(registrationDto.getJob())
                .havePet(registrationDto.isHavePet())
                .petInfo(registrationDto.getPetInfo())
                .tempProtect(registrationDto.getTempProtect())
                .tempPeriod(registrationDto.getTempPeriod())
                .regStatus(registrationDto.getRegStatus())
                .meetPlace(registrationDto.getMeetPlace())
                .meetTime(registrationDto.getMeetTime())
                .meetMaterial(registrationDto.getMeetMaterial())
                .validReg(registrationDto.isValidReg())
                .userCheck(registrationDto.isUserCheck())
                .type(registrationDto.getType())
                .build();
    }


}
