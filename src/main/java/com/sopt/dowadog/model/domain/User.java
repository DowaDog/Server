package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateEntity {

    @Id
    private String id;

    private String password;
    private String nickname;
    private String name;
    private String birth;
    private String phone;
    private String email;
    private String gender;
    private String deviceToken;
    private String type;
    private String profileImg;
    private boolean pushAllow;


    @Transient
    @JsonIgnore
    private MultipartFile profileImgFile;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<Community> community;

    @ManyToMany
    @JoinTable(name="user_cardnews_scrap", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="cardnews_id"))
    private List<Cardnews> scrapedCardnews = new ArrayList<Cardnews>();


    @ManyToMany
    @JoinTable(name="user_cardnews_education", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="cardnews_id"))
    private List<Cardnews> educatedCardnews = new ArrayList<Cardnews>();
}

