package com.sopt.dowadog.model.domain;

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
public class Registration extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean status = true; // 신청서가 진행중인지 (true false)

    //todo 신청서 정보들 기획 나오면 추가될 칼럼들
    private String address;
    private String job;
    private boolean tempProtect; //임시보호 여부

    private String regStatus; // 현재 상태


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registration")
    private RegistrationMeeting registrationMeeting;

    @ManyToOne
    @JoinColumn(nullable=false)
    private User user;

    @ManyToOne
    private Animal animal;


    private boolean userCheck = false; //유저가 메인 뷰 확인했는지, admin이 요청 할때마다 false된다.


}
