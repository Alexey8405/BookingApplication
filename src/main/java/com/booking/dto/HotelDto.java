package com.booking.dto;

import lombok.Data;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String advertisementTitle;
    private String city;
    private String address;
    private Double distanceFromCenter;
}