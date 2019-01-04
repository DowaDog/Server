//package com.sopt.dowadog.model.domain;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class RegistrationMeeting {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String place;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date time;
//
//    @OneToOne
//    private Registration registration;
//
//    private String material;
//
//}
