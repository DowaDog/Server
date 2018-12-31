package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class UserAnimalId implements Serializable {

    private String user;
    private Integer animal;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        UserAnimalId that = (UserAnimalId) o;
//        return Objects.equals(user, that.user) &&
//                Objects.equals(animal, that.animal);
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(user, animal);
//    }
}
