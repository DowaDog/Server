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
    private String name;
    private String birth;
    private String phone;
    private String email;
    private String gender;
    private String deviceToken;
    private String type;
    private String profileImg;
    private boolean pushAllow;
    private boolean termsAllow;


    @Transient
    @JsonIgnore
    private MultipartFile profileImgFile;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<Community> communityList;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<Registration> registrationList;

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    private List<Mailbox> mailboxList;



    @OneToMany(mappedBy = "user")
    private List<UserAnimalLike> userAnimalLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCardnewsScrap> userCardnewsScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCardnewsEducate> userCardnewsEducatedList = new ArrayList();

    @JsonIgnore
    public int getAnimalLikeCount() {
        return this.userAnimalLikeList.size();
    }

    @JsonIgnore
    public int getCardnewsScrapCount() {
        return this.userCardnewsScrapList.size();
    }

    @JsonIgnore
    public int getWrittenCommunityCount() {
        return this.communityList.size();
    }

    @JsonIgnore
    public boolean isNewMailbox() {
        for(Mailbox mailbox : mailboxList){
            if(!mailbox.isComplete()) return true;
        }
        return false;
    }

    //todo 태경 추가 코드 확인 필요 : @ 26살 성찬쓰

    @OneToMany(mappedBy="user")  //즉시로딩인것 같아서 fetch 값 설정 안함
    private List<AnimalUserAdopt> animalUserAdoptList = new ArrayList<>();

    @JsonIgnore //사용자 입양 동물중 리스트 최신 제일 위에 있는 동물의 이름

    public String getUserAdoptAnimalName() {return this.animalUserAdoptList.get(animalUserAdoptList.size()-1).getName();}
//animaluser의 필드값을 가져와야한다.


}

