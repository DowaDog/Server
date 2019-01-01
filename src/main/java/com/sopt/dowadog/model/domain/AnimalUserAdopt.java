package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Temporal(TemporalType.DATE)
    private Date birth;
    private int weight;
    private boolean neuterYn;

    private String adoptType; // 임보, 입양 만약 입보상태에서 같은 animal이 임양상태의 값으로 생성되면 삭제되어야함. 그리고 해당 유저한테 메시지 가도록 처리되야할듯.

    @OneToMany(mappedBy="animalUserAdopt", fetch = FetchType.LAZY)
    private List<AnimalCheckup> animalCheckupList;

    @ManyToOne
    private User user;

    @OneToOne
    private Registration registration;


}
