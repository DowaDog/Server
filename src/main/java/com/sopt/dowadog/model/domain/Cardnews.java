package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mysql.cj.protocol.ColumnDefinition;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.CardnewsDto;
import com.sopt.dowadog.model.dto.UserCardnewsEducateDto;
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

    @Transient
    @JsonIgnore
    private MultipartFile cardnewsImgFile;

    @OneToMany(mappedBy = "cardnews")
    private List<UserCardnewsEducate> userCardnewsEducateList = new ArrayList<>();

    @OneToMany(mappedBy = "cardnews")
    private List<UserCardnewsScrap> userCardnewsScrapList = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    User user;

    @JsonIgnore
    public CardnewsDto getCardnewsDto() {
        return CardnewsDto.builder()
                .id(this.id)
                .title(this.title)
                .subtitle(this.subtitle)
                .type(this.type)
                .imgPath(this.imgPath)
                .build();
    }

    public boolean getAuth(String userId){
        return userId.equals(this.user.getId());
    }

    public boolean getEducated(User user){

        for(UserCardnewsEducate cardnewsEducate : user.getUserCardnewsEducatedList()){
            if(this.id == cardnewsEducate.getCardnews().getId()) return true;
        }
        return false;
    }




}

