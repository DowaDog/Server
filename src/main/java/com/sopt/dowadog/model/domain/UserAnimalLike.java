package com.sopt.dowadog.model.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.AnimalDetailDto;
import com.sopt.dowadog.model.dto.ListformDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserAnimalId.class)
public class UserAnimalLike extends DateEntity {

    @JsonIgnore
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Animal animal;

    @JsonIgnore
    public AnimalDetailDto getAnimalDetailDto() {
        return animal.getAnimalDetailDto();
    }

    @JsonIgnore
    public ListformDto getListformDto() {
        return animal.getListAnimalDto();
    }


}
