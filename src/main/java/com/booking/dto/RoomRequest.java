package com.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Room number is required")
    private String number;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Maximum people is required")
    private Integer maxPeople;

    private List<String> unavailableDates;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;
}