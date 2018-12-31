package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mysql.cj.protocol.ColumnDefinition;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cardnews extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String subtitle;

    private String type;

    private String imgPath;


    @Transient
    private boolean complete;

    @OneToMany(mappedBy = "cardnews")
    private List<UserCardnewsEducate> userCardnewsEducateList = new ArrayList<>();

    @OneToMany(mappedBy = "cardnews")
    private List<UserCardnewsScrap> userCardnewsScrapList = new ArrayList<>();

}

