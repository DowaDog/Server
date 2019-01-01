package com.sopt.dowadog.specification;

import com.sopt.dowadog.enumeration.PublicAnimalTypeEnum;
import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.AnimalStory;
import com.sopt.dowadog.model.domain.PublicAnimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class AnimalSpecification {
    public static Specification<Animal> searchAnimal(Map<String, Object> filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            filter.forEach((key, value) -> {

                switch (key) {
                    case "type":
                        Predicate typePredicate =
                                cb.like(root.get("type").as(String.class),
                                        new StringBuilder(value.toString()).append("%").toString());
                        predicates.add(typePredicate);
                        break;
                    case "region":
                        Predicate regionPredicate =
                                cb.like(root.get("care").get("region").as(String.class),
                                        new StringBuilder(value.toString()).append("%").toString());
                        predicates.add(regionPredicate);
                        break;
                    case "remainNoticeDate":
                        System.out.print(root.get("noticeEddt").as(LocalDate.class).toString());

                        Predicate remainDatePredicate =
                                cb.lessThanOrEqualTo(root.get("noticeEddt").as(LocalDate.class),
                                        LocalDate.now().plusDays((int)value));
                        Predicate remainDatePredicate1 =
                                cb.greaterThan(root.get("noticeEddt").as(LocalDate.class),LocalDate.now());
                        Predicate totalremainDate =
                               cb.and(remainDatePredicate,remainDatePredicate1);
                        predicates.add(totalremainDate);
                        break;
                    case "searchWord":
                        Predicate searchingWord =
                                cb.like(root.get("care").get("region").as(String.class), new StringBuilder("%").append(value.toString()).append("%").toString());
                        Predicate searchingWord1 =
                                cb.like(root.get("care").get("name").as(String.class),new StringBuilder("%").append(value.toString()).append("%").toString());
                        Predicate searchingWord2 =
                                cb.like(root.get("kindCd").as(String.class),new StringBuilder("%").append(value.toString()).append("%").toString());

                        Predicate totalList1 =
                                cb.or(searchingWord,searchingWord1,searchingWord2);
                        predicates.add(totalList1);
                        break;
                    case "story":

                        Predicate storyList =
                                cb.isNotEmpty(root.get("animalStoryList"));
                        predicates.add(storyList);
                        break;

                    case "tag" :
                       /*Predicate tagList =
                                cb.*/



                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
