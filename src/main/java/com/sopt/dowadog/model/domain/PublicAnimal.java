package com.sopt.dowadog.model.domain;


import com.sopt.dowadog.model.dto.PublicAnimalDetailDto;
import com.sopt.dowadog.model.dto.PublicListformDto;
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
public class PublicAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String noticeNo;

    private String age;
    private String careAddr;
    private String careNm;
    private String careTel;
    private String chargeNm;
    private String colorCd;
    private String desertionNo;
    private String filename;
    private String profile;
    private String happenDt;
    private String happenPlace;
    private String kindCd;
    private String neuterYn;

    private String noticeEdt;
    private String noticeSdt;

    private String specialMark;
    private String processState;
    private String sexCd;
    private String weight;
    private String officetel;
    private String popfile;
    private String orgNm;
    private String noticeComment;

    //동물 리스트 메소드
    public PublicListformDto getListAnimalDto(){
        PublicListformDto listformDto = PublicListformDto.builder()
                .id(this.id)
                .noticeNo(this.noticeNo)
                .sexCd(this.sexCd)
                .kindCd(this.kindCd)
                .thumbnailImg(this.popfile)
                .build();
        return listformDto;
    }

    //상세보기 메소드
    public PublicAnimalDetailDto getPublicAnimalDetailDto(){
        PublicAnimalDetailDto publicAnimalDetailDto = PublicAnimalDetailDto.builder()
                .id(this.id)
                .weight(this.weight)
                .age(this.age)
                .careName(this.careNm)
                .careTel(this.careTel)
                .noticeNo(this.noticeNo)
                .happenPlace(this.happenPlace)
                .processState(this.processState)
                .sexCd(this.sexCd)
                .specialMark(this.specialMark)
                .thumbnailImg(this.popfile)
                .build();
        return publicAnimalDetailDto;
    }

}
