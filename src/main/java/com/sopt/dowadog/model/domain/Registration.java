package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.*;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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
    private boolean tempProtect; // adopt, temp
    private String tempPeriod;
    private String regStatus; // deny, step0, step1, step2, complete
    private String meetPlace;
    private String meetTime;
    private String meetMaterial;
    private boolean validReg = true; // true 진행중, false 종료
    private boolean userCheck = true; //true 유저가 확인함, false 유저가 확인 안함

    @ManyToOne
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JsonManagedReference
    private Animal animal;





}
