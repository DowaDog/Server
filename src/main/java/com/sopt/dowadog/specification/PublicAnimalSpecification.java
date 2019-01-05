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
                                        new StringBuilder("[").append(value.toString()).append("%").toString());
                        predicates.add(typePredicate);
                        break;
                    case "region":
                        Predicate regionPredicate =
                                cb.like(root.get("orgNm").as(String.class),
                                        new StringBuilder(value.toString()).append("%").toString());
                        predicates.add(regionPredicate);
                        break;
                    case "remainNoticeDate":
                        Predicate remainDatePredicate =
                                cb.lessThanOrEqualTo(root.get("noticeEdt").as(LocalDate.class),
                                        LocalDate.now().plusDays((int)value));
                        predicates.add(remainDatePredicate);
                        break;
                    case "searchWord" :
                        Predicate searchWord1 = cb.like(root.get("careNm").as(String.class),
                                new StringBuilder("%").append(value.toString()).append("%").toString());
                        Predicate searchWord2 = cb.like(root.get("orgNm").as(String.class),
                                new StringBuilder(value.toString()).append("%").toString());
                        Predicate searchWord3 = cb.like(root.get("kindCd").as(String.class),
                                new StringBuilder("%").append(value.toString()).append("%").toString());
                        Predicate resultWord =
                                cb.or(searchWord1,searchWord2,searchWord3);
                        predicates.add(resultWord);
                        break;
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}

