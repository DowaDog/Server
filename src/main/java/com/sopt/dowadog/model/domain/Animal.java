package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.*;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.AnimalDetailDto;
import com.sopt.dowadog.model.dto.ListformDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Animal.class)
@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String type; // dog, cat

//    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeEddt;

    private String processState = "notice"; // 입양 공고 진행 상태 , notice : 공고중, step : 입양절차 진행중, adopt : 입양됨, temp : 임보됨, end : 안락사
    private String sexCd;
    private Boolean neuterYn;
    private String specialMark;
    private String happenPlace;
    private String kindCd;
    private String age;
    private String weight;

    //@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noticeStdt;

    private String thumbnailImg;

    @Transient
    private boolean liked;

    @ManyToOne
    @JsonManagedReference
    private Care care;


    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<AnimalStory> animalStoryList;

//    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
//    private List<Registration> registrationList;



    // Dto builder 작업(상세보기 dto)
    @JsonIgnore
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
    @JsonIgnore
    public ListformDto getListAnimalDto(){
        ListformDto listformDto = ListformDto.builder()
                .id(this.id)
                .type(this.type)
                .sexCd(this.sexCd)
                .kindCd(this.kindCd)
                .region(this.care.getRegion())
                .noticeEddt(this.noticeEddt)
                .processState(this.processState)
                .build();

        return listformDto;
    }

    @Transient
    @JsonIgnore
    private MultipartFile thumbnailImgFile;


    //@OneToMany(mappedBy="animal")
  //  private List<UserAnimalLike> userAnimalLikeList;//이거 굳이 필요해..? ㅠ... 동물을 기준으로 좋아요 한 사람을 쭉 ...보여주는...정보... 필요한가....




//    @OneToMany(mappedBy="animal", fetch=FetchType.LAZY)
//    private List<HashtagAnimal> hashtagList;

    /*@OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<HashtagAnimal> hashtagAnimalList;*/

}
