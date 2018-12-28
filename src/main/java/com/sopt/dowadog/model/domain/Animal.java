package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String type;

    @Temporal(TemporalType.DATE)
    private Date noticeEddt;

    private String processState;
    private String sexCd;
    private String neuterYn;
    private String specialMark;
    private String happenPlace;
    private String kindCd;
    private String age;
    private String weight;

    @Temporal(TemporalType.DATE)
    private Date noticeStdt;

    private String thumbnailImg;

    @ManyToOne
    private Care care;

    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
    private List<Registration> registration;


}
