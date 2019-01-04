package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Care.class)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Care extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*private String careUserId;
    private String password;
*/

    private String region;
    private String address;
    private String name;
    private String tel;

    private int status;


    @OneToMany(mappedBy = "care", fetch = FetchType.LAZY)
    private List<Animal> animalList;

}
