package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
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
public class AnimalStory extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Animal animal;

    private String filePath;
    private String originFileName;
}
