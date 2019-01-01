package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class UserCardnewsId implements Serializable {

    private String user;
    private Integer cardnews;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCardnewsId that = (UserCardnewsId) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(cardnews, that.cardnews);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, cardnews);
    }

}
