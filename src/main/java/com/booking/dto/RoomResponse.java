package com.booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private String number;
    private BigDecimal price;
    private Integer maxPeople;
    private Long hotelId;
    private String hotelName;
}