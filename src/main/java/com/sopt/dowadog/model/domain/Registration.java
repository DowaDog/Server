package com.sopt.dowadog.model.domain;

import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //todo 신청서 정보들 기획 나오면 추가될 칼럼들
    private String address;
    private String job;
    private boolean stepOneAllow;
    private boolean tempProtect; //임시보호 여부


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registration")
    private RegistrationMeeting registrationMeeting;

    @ManyToOne
    @JoinColumn(nullable=false)
    private User user;

    @ManyToOne
    private Animal animal;


}
