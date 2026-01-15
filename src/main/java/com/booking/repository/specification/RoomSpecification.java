package com.booking.repository.specification;

import com.booking.dto.RoomFilterRequest;
import com.booking.entity.Booking;
import com.booking.entity.Room;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static Specification<Room> filter(RoomFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (StringUtils.hasText(filter.getTitle())) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getTitle().toLowerCase() + "%"
                ));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("price"),
                        filter.getMinPrice()
                ));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("price"),
                        filter.getMaxPrice()
                ));
            }

            if (filter.getGuests() != null) {
                predicates.add(cb.equal(
                        root.get("maxPeople"),
                        filter.getGuests()
                ));
            }

            if (filter.getHotelId() != null) {
                predicates.add(cb.equal(
                        root.get("hotel").get("id"),
                        filter.getHotelId()
                ));
            }

            if (filter.getCheckInDate() != null && filter.getCheckOutDate() != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Booking> bookingRoot = subquery.from(Booking.class);

                Predicate roomMatch = cb.equal(bookingRoot.get("room").get("id"), root.get("id"));
                Predicate dateOverlap = cb.and(
                        cb.lessThan(bookingRoot.get("checkInDate"), filter.getCheckOutDate()),
                        cb.greaterThan(bookingRoot.get("checkOutDate"), filter.getCheckInDate())
                );

                subquery.select(cb.literal(1L))
                        .where(cb.and(roomMatch, dateOverlap));

                predicates.add(cb.not(cb.exists(subquery)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}