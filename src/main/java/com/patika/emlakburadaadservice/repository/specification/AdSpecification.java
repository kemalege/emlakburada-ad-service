package com.patika.emlakburadaadservice.repository.specification;

import com.patika.emlakburadaadservice.dto.request.AdSearchRequest;
import com.patika.emlakburadaadservice.model.Ad;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdSpecification {

    public static Specification<Ad> initAdSpecification(AdSearchRequest request) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicateList = new ArrayList<>();

            if (request.getCustomerId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("userId"), request.getCustomerId()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
