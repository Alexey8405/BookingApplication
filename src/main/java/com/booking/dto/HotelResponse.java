package com.booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String advertisementTitle;
    private String city;
    private String address;
    private Double distanceFromCenter;
    private BigDecimal rating;
    private Integer ratingCount;
}