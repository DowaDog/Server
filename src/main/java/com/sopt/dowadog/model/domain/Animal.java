package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.AnimalDetailDto;
import com.sopt.dowadog.model.dto.ListformDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


//@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Animal.class)
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

    //@Temporal(TemporalType.DATE)
    private LocalDate noticeEddt;

    private String processState;
    private String sexCd;
    private String neuterYn;
    private String specialMark;
    private String happenPlace;
    private String kindCd;
    private String age;
    private String weight;
    private boolean noticing; // 입양 공고 진행 여부

    //@Temporal(TemporalType.DATE)
    private LocalDate noticeStdt;

    private String thumbnailImg;

    @Transient
    private boolean liked;

    @ManyToOne
    private Care care;


    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<AnimalStory> animalStoryList;

    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
    private List<Registration> registrationList;



    // Dto builder 작업(상세보기 dto)
    public AnimalDetailDto getAnimalDetailDto() {


        // 좋아요랑 썸네일 파일,  스토리 리스트, 날짜 여부
        AnimalDetailDto animalDetailDto = AnimalDetailDto.builder()
                .age(this.age)
                .careName(this.care.getName())
                .careTel(this.care.getTel())
                .happenPlace(this.happenPlace)
                .id(this.id)
                .sexCd(this.sexCd)
                .kindCd(this.kindCd)
                .noticeEddt(this.noticeEddt)
                .noticeStdt(this.noticeStdt)
                .processState(this.processState)
                .specialMark(this.specialMark)
                .region(this.care.getRegion())
                .type(this.type)
                .weight(this.weight)
                .build();

        return animalDetailDto;
    }


    //리스트폼 dto

    public ListformDto getListAnimalDto(){
        ListformDto listformDto = ListformDto.builder()
                .id(this.id)
                .type(this.type)
                .sexCd(this.sexCd)
                .kindCd(this.kindCd)
                .region(this.care.getRegion())
                .noticeEddt(this.noticeEddt)
                .build();

        return listformDto;
    }


    //@OneToMany(mappedBy="animal")
  //  private List<UserAnimalLike> userAnimalLikeList;//이거 굳이 필요해..? ㅠ... 동물을 기준으로 좋아요 한 사람을 쭉 ...보여주는...정보... 필요한가....




//    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
//    private List<HashtagAnimal> hashtagList;

    /*@OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<HashtagAnimal> hashtagAnimalList;*/

}
