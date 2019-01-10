package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalUserAdopt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String gender;
    private String kind;
    private String age;
    private String weight;
    private boolean neuterYn;
    private String profileImg;


//    private String adoptType; // 임보, 입양 만약 입보상태에서 같은 animal이 임양상태의 값으로 생성되면 삭제되어야함. 그리고 해당 유저한테 메시지 가도록 처리되야할듯.

    @Transient
    private String[] inoculationArray;

    @JsonIgnore
    @Transient
    private MultipartFile profileImgFile;

    @JsonIgnore
    @OneToMany(mappedBy="animalUserAdopt", fetch = FetchType.LAZY)
    private List<AnimalCheckup> animalCheckupList;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToOne
    private Registration registration;

    @JsonIgnore
    public boolean getAuth(String userId){
        return userId.equals(this.user.getId());
    }

    public String getAnimalType() {
        return this.registration.getAnimal().getType();
    }


}
