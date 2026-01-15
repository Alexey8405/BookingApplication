package com.booking.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomDto {
    private Long id;
    private String name;
    private String description;
    private String number;
    private BigDecimal price;
    private Integer maxPeople;
}