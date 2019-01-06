package com.sopt.dowadog.model.domain;

import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserCardnewsId.class)
public class UserCardnewsScrap extends DateEntity {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Cardnews cardnews;

}
