package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.*;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;


//@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=AnimalStory.class)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalStory extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @JsonIgnore // 다시 한번 생각해봐야함.
    @ManyToOne
    private Animal animal;

    private String filePath;
    private String originFileName;

    @Transient
    @JsonIgnore
    List<MultipartFile> animalStoryFiles;
}
