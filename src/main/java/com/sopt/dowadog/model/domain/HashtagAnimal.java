package com.sopt.dowadog.model.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@IdClass(HashtagAnimalId.class)
public class HashtagAnimal {


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @ManyToOne
    private Hashtag hashtag;

    @Id
    @ManyToOne
    private Animal animal;


}


