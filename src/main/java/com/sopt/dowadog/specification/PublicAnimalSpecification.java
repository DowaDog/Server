package com.sopt.dowadog.specification;

import com.sopt.dowadog.enumeration.PublicAnimalTypeEnum;
import com.sopt.dowadog.model.domain.PublicAnimal;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PublicAnimalSpecification {

    public static Specification<PublicAnimal> searchPublicAnimal(Map<String, Object> filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            filter.forEach((key, value) -> {

                switch (key) {
                    case "type":
                        Predicate typePredicate =
                                cb.like(root.get("kindCd").as(String.class),
                                        new StringBuilder("[").append(PublicAnimalTypeEnum.valueOf(value.toString()).getValue()).append("%").toString());
                        predicates.add(typePredicate);
                        break;
                    case "region":
                        Predicate regionPredicate =
                                cb.like(root.get("noticeNo").as(String.class),
                                        new StringBuilder(value.toString()).append("%").toString());
                        predicates.add(regionPredicate);
                        break;
                    case "remainNoticeDate":
                        Predicate remainDatePredicate =
                                cb.lessThan(root.get("noticeEdt").as(LocalDate.class),
                                        LocalDate.now().plusDays((int)value));
                        predicates.add(remainDatePredicate);
                        break;
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}

