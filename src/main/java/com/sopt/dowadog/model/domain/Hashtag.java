package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String keyword;


    @ManyToMany
    @JoinTable(name="hashtag_animal", joinColumns = @JoinColumn(name="hashtag_id"), inverseJoinColumns = @JoinColumn(name="animal_id"))
    private List<Animal> hashtagAnimalList = new ArrayList<Animal>();

}
