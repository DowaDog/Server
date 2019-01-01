package com.sopt.dowadog.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
public class HashtagAnimalId implements Serializable {

    private String hashtag;
    private Integer animal;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashtagAnimalId that = (HashtagAnimalId) o;
        return Objects.equals(hashtag, that.hashtag) &&
                Objects.equals(animal, that.animal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hashtag, animal);
    }
}
