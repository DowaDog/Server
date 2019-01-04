package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.MainDto;
import com.sopt.dowadog.model.dto.MyinfoDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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


    @Transient
    @JsonIgnore
    private MultipartFile profileImgFile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Community> communityList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Registration> registrationList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Mailbox> mailboxList;


    @OneToMany(mappedBy = "user")
    private List<UserAnimalLike> userAnimalLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCardnewsScrap> userCardnewsScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserCardnewsEducate> userCardnewsEducatedList = new ArrayList();

    @OneToMany(mappedBy = "user")
    private List<AnimalUserAdopt> animalUserAdoptList = new ArrayList<>();


    @JsonIgnore
    public int getCardnewsEducatedCount() { return this.userCardnewsEducatedList.size(); }

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
        for (Mailbox mailbox : mailboxList) {
            if (!mailbox.isComplete()) return true;
        }
        return false;
    }

    @JsonIgnore //사용자 입양 동물중 리스트 최신 제일 위에 있는 동물의 이름
    public String getUserAdoptAnimalName() {

        if(this.animalUserAdoptList.size() != 0){
            return this.animalUserAdoptList.get(this.animalUserAdoptList.size()-1).getName();
        } else {
            return null; // 입양동물 없을경우 null
        }
    }

    //todo 태경 추가 코드 확인 필요 : @ 26살 성찬쓰


//animaluser의 필드값을 가져와야한다.

    @JsonIgnore
    public MainDto getMainDto() {
        return MainDto.builder()
                .login(true)
                .mailboxUpdated(isNewMailbox())
                .build();
    }

    @JsonIgnore
    public MyinfoDto getMyinfoDto() {
        MyinfoDto myinfoDto =  MyinfoDto.builder()
                .userName(this.name)
                .userLike(this.getAnimalLikeCount())
                .userScrap(this.getCardnewsScrapCount())
                .userCommunity(this.getWrittenCommunityCount())
                .mailboxUpdated(this.isNewMailbox())
                .animalName(this.getUserAdoptAnimalName())
                .build();

        return myinfoDto;

    }


    public boolean getAuth(String userId){
        return userId.equals(this.id);
    }

}

