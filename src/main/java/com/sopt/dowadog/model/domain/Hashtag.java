package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Animal.class)
@Entity
@Data
@Builder
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String keyword;

    //@ElementCollection
    //@OrderBy("animal.createAt")
    @OneToMany(mappedBy="hashtag", fetch=FetchType.LAZY)
    private List<HashtagAnimal> hashtagAnimalList;




    //@ManyToMany
    //@JoinTable(name="hashtag_animal", joinColumns = @JoinColumn(name="hashtag_id"), inverseJoinColumns = @JoinColumn(name="animal_id"))
    //private List<Animal> hashtagAnimalList = new ArrayList<Animal>();

}
