package com.booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class HotelFilterRequest {
    private Long id;
    private String name;
    private String advertisementTitle;
    private String city;
    private String address;
    private Double distanceFromCenterMin;
    private Double distanceFromCenterMax;
    private BigDecimal ratingMin;
    private BigDecimal ratingMax;
    private Integer ratingCountMin;
    private Integer ratingCountMax;
    private Integer page = 0;
    private Integer size = 10;
}