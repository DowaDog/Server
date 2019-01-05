package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.UserCardnewsEducateDto;
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
@IdClass(UserCardnewsId.class)
public class UserCardnewsEducate extends DateEntity {
    @Id
    @JsonIgnore
    @ManyToOne
    private User user;

    @Id
    @JsonIgnore
    @ManyToOne
    private Cardnews cardnews;

    @JsonIgnore
    public UserCardnewsEducateDto getUserCardnewsEducateDto() {
        return UserCardnewsEducateDto.builder()
                .cardnewsId(this.cardnews.getId())
                .userId(this.user.getId())
                .build();
    }

}
