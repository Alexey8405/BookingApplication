package com.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RoomFilterRequest {
    private Long id;
    private String title;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer guests;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    private Long hotelId;
    private Integer page = 0;
    private Integer size = 10;
}