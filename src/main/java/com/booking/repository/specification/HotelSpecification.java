package com.booking.repository.specification;

import com.booking.dto.HotelFilterRequest;
import com.booking.entity.Hotel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> filter(HotelFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (StringUtils.hasText(filter.getName())) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(filter.getAdvertisementTitle())) {
                predicates.add(cb.like(
                        cb.lower(root.get("advertisementTitle")),
                        "%" + filter.getAdvertisementTitle().toLowerCase() + "%"
                ));
            }

            if (StringUtils.hasText(filter.getCity())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("city")),
                        filter.getCity().toLowerCase()
                ));
            }

            if (StringUtils.hasText(filter.getAddress())) {
                predicates.add(cb.like(
                        cb.lower(root.get("address")),
                        "%" + filter.getAddress().toLowerCase() + "%"
                ));
            }

            if (filter.getDistanceFromCenterMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("distanceFromCenter"),
                        filter.getDistanceFromCenterMin()
                ));
            }

            if (filter.getDistanceFromCenterMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("distanceFromCenter"),
                        filter.getDistanceFromCenterMax()
                ));
            }

            if (filter.getRatingMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("rating"),
                        filter.getRatingMin()
                ));
            }

            if (filter.getRatingMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("rating"),
                        filter.getRatingMax()
                ));
            }

            if (filter.getRatingCountMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("ratingCount"),
                        filter.getRatingCountMin()
                ));
            }

            if (filter.getRatingCountMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("ratingCount"),
                        filter.getRatingCountMax()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}